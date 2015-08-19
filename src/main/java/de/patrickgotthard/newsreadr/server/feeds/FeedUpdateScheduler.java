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

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserEntryRepository;

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
    public void updateFeeds() {
        final List<Feed> feeds = this.feedRepository.findAll();
        LOG.info("Updating {} feeds", feeds.size());
        feeds.forEach(feed -> this.updateFeed(feed.getId()));
        LOG.info("Finished updating feeds");
    }

    /**
     * Updates the given feed in a new transaction.
     *
     * @param feed The feed to update
     * @throws FetcherServiceException when fetching the feed failed
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void updateFeed(final long feedId) {

        LOG.debug("Updating feed {}", feedId);

        try {

            final Feed feed = this.feedRepository.findOne(feedId);

            // could happen when a feed was deleted while the scheduler runs
            if (feed != null) {

                final String feedUrl = feed.getUrl();

                final Feed fetchedFeed = this.feedService.fetch(feedUrl);
                final Set<Entry> entries = fetchedFeed.getEntries();

                final Collection<Entry> newEntries = this.extractNewEntries(feed, entries);
                this.persistEntries(feed, newEntries);

                this.feedRepository.save(feed);

            }

        } catch (final Exception e) {
            LOG.info("An error occured while updating feed {}", feedId);
        }

        LOG.debug("Finished updating feed {}", feedId);

    }

    /**
     * Extracts new entries.
     *
     * @param feed The feed the entries should belong to
     * @param entries The entries to process
     * @return New entries
     */
    private Collection<Entry> extractNewEntries(final Feed feed, final Set<Entry> entries) {

        final List<Entry> newEntries = new ArrayList<>();

        entries.forEach(entry -> {

            final String uri = entry.getUri();

            final BooleanBuilder filter = new BooleanBuilder();
            filter.and(QEntry.entry.feed.eq(feed));
            filter.and(QEntry.entry.uri.eq(uri));

            final boolean newEntry = this.entryRepository.count(filter) == 0;
            if (newEntry) {
                newEntries.add(entry);
            }

        });

        return newEntries;

    }

    /**
     * Persists the given entries.
     *
     * @param feed The feed the entries should belong to
     * @param entries The entries that should be persisted
     */
    private void persistEntries(final Feed feed, final Collection<Entry> entries) {

        if (!entries.isEmpty()) {

            final BooleanExpression referencesFeed = QSubscription.subscription.feed.eq(feed);
            final List<Subscription> subscriptions = this.subscriptionRepository.findAll(referencesFeed);

            entries.forEach(entry -> {
                entry.setFeed(feed);
                entry = this.entryRepository.save(entry);
                this.createUserEntries(entry, subscriptions);
            });

        }

    }

    /**
     * Creates new user entries.
     *
     * @param entry The entry to process
     * @param subscribers The subscribers to create the user entries for.
     */
    private void createUserEntries(final Entry entry, final List<Subscription> subscriptions) {
        subscriptions.forEach(subscription -> {
            final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).setEntry(entry).setRead(false).setBookmarked(false).build();
            this.userEntryRepository.save(userEntry);
        });
    }

}
