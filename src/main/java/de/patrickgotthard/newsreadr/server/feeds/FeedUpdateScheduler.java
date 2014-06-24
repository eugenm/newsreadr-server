package de.patrickgotthard.newsreadr.server.feeds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.entries.Entry;
import de.patrickgotthard.newsreadr.server.entries.EntryRepository;
import de.patrickgotthard.newsreadr.server.subscriptions.Subscription;
import de.patrickgotthard.newsreadr.server.subscriptions.SubscriptionExpression;
import de.patrickgotthard.newsreadr.server.subscriptions.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.userentries.UserEntry;
import de.patrickgotthard.newsreadr.server.userentries.UserEntryRepository;

@Component
class FeedUpdateScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(FeedUpdateScheduler.class);

    private final FeedRepository feedRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EntryRepository entryRepository;
    private final UserEntryRepository userEntryRepository;
    private final FeedService feedService;

    @Autowired
    public FeedUpdateScheduler(final FeedRepository feedRepository, final SubscriptionRepository subscriptionRepository, final EntryRepository entryRepository,
            final UserEntryRepository userEntryRepository, final FeedService feedService) {
        this.feedRepository = feedRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.entryRepository = entryRepository;
        this.userEntryRepository = userEntryRepository;
        this.feedService = feedService;
    }

    /**
     * Updates all feeds every 5 minutes.
     */
    @Scheduled(initialDelay = 15_000, fixedDelay = 300_000)
    @Transactional
    void updateFeeds() {
        LOG.debug("Updating feeds");
        final List<Feed> feedsToUpdate = feedRepository.findAll();
        for (final Feed feed : feedsToUpdate) {
            final Long feedId = feed.getId();
            updateFeed(feedId);
        }
        LOG.debug("Finished updating feeds");
    }

    /**
     * Updates the given feed in a new transaction.
     *
     * @param feed
     *            The feed to update
     * @throws FetcherServiceException
     *             when fetching the feed failed
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void updateFeed(final long feedId) {

        LOG.debug("Updating feed {}", feedId);

        try {

            final Feed feed = feedRepository.findOne(feedId);

            // could happen when a feed was deleted while the scheduler runs
            if (feed != null) {

                final String feedUrl = feed.getUrl();

                final Feed fetchedFeed = feedService.fetch(feedUrl);
                final Set<Entry> entries = fetchedFeed.getEntries();

                final Collection<Entry> newEntries = extractNewEntries(feed, entries);
                persistEntries(feed, newEntries);

                feedRepository.save(feed);

            }

        } catch (final Exception e) {
            LOG.info("An error occured while updating feed " + feedId, e);
        }

        LOG.debug("Finished updating feed {}", feedId);

    }

    /**
     * Extracts new entries.
     *
     * @param feed
     *            The feed the entries should belong to
     * @param entries
     *            The entries to process
     * @return New entries
     */
    private Collection<Entry> extractNewEntries(final Feed feed, final Set<Entry> entries) {
        final List<Entry> newEntries = new ArrayList<>();
        for (final Entry entry : entries) {
            final String uri = entry.getUri();
            final boolean newEntry = entryRepository.countByFeedAndUri(feed, uri) == 0;
            if (newEntry) {
                newEntries.add(entry);
            }
        }
        return newEntries;
    }

    /**
     * Persists the given entries.
     *
     * @param feed
     *            The feed the entries should belong to
     * @param entries
     *            The entries that should be persisted
     */
    private void persistEntries(final Feed feed, final Collection<Entry> entries) {

        if (!entries.isEmpty()) {

            final BooleanExpression referencesFeed = SubscriptionExpression.referencesFeed(feed);
            final List<Subscription> subscriptions = subscriptionRepository.findAll(referencesFeed);

            for (Entry entry : entries) {

                entry.setFeed(feed);
                entry = entryRepository.save(entry);

                createUserEntries(entry, subscriptions);

            }

        }

    }

    /**
     * Creates new user entries.
     *
     * @param entry
     *            The entry to process
     * @param subscribers
     *            The subscribers to create the user entries for.
     */
    private void createUserEntries(final Entry entry, final List<Subscription> subscriptions) {
        for (final Subscription subscription : subscriptions) {
            final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).setEntry(entry).setRead(false).setBookmarked(false).build();
            userEntryRepository.save(userEntry);
        }
    }

}
