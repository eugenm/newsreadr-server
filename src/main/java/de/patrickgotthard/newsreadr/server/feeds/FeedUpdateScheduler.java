package de.patrickgotthard.newsreadr.server.feeds;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

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
import de.patrickgotthard.newsreadr.server.common.tx.TransactionHelper;

@Component
class FeedUpdateScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(FeedUpdateScheduler.class);

    private final FeedRepository feedRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EntryRepository entryRepository;
    private final UserEntryRepository userEntryRepository;
    private final FeedService feedService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public FeedUpdateScheduler(final FeedRepository feedRepository, final SubscriptionRepository subscriptionRepository, final EntryRepository entryRepository,
        final UserEntryRepository userEntryRepository, final FeedService feedService, final TransactionHelper transactionHelper) {
        this.feedRepository = feedRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.entryRepository = entryRepository;
        this.userEntryRepository = userEntryRepository;
        this.feedService = feedService;
        this.transactionHelper = transactionHelper;
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
    private void updateFeed(final long feedId) {

        LOG.debug("Updating feed {}", feedId);

        try {

            this.transactionHelper.executeInNewTransaction(() -> {

                final Feed feed = FeedUpdateScheduler.this.feedRepository.findOne(feedId);
                if (feed != null) {

                    final String feedUrl = feed.getUrl();

                    final Feed fetchedFeed = this.feedService.fetch(feedUrl);
                    final Set<Entry> entries = fetchedFeed.getEntries();

                    final Collection<Entry> newEntries = this.extractNewEntries(feed, entries);
                    this.persistEntries(feed, newEntries);

                    this.feedRepository.save(feed);

                }

                return null;

            });

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

        return entries.parallelStream().filter(entry -> {

            final BooleanBuilder filter = new BooleanBuilder();
            filter.and(QEntry.entry.feed.eq(feed));
            filter.and(QEntry.entry.uri.eq(entry.getUri()));
            return !this.entryRepository.exists(filter);

        }).collect(toList());

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

            final UserEntry userEntry = new UserEntry();
            userEntry.setSubscription(subscription);
            userEntry.setEntry(entry);
            userEntry.setRead(false);
            userEntry.setBookmarked(false);

            this.userEntryRepository.save(userEntry);

        });
    }

}
