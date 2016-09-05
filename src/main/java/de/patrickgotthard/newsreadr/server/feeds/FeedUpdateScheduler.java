package de.patrickgotthard.newsreadr.server.feeds;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.rometools.rome.feed.synd.SyndFeed;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.common.tx.TransactionHelper;

@Component
class FeedUpdateScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(FeedUpdateScheduler.class);

    private final SubscriptionRepository subscriptionRepository;
    private final EntryRepository entryRepository;
    private final FeedService feedService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public FeedUpdateScheduler(final SubscriptionRepository subscriptionRepository, final EntryRepository entryRepository, final FeedService feedService,
        final TransactionHelper transactionHelper) {
        this.subscriptionRepository = subscriptionRepository;
        this.entryRepository = entryRepository;
        this.feedService = feedService;
        this.transactionHelper = transactionHelper;
    }

    /**
     * Updates feeds every 5 minutes
     */
    @Scheduled(initialDelay = 15_000, fixedDelay = 300_000)
    @Transactional
    public void updateFeeds() {
        LOG.info("Updating feeds");
        this.subscriptionRepository.findAll().parallelStream().forEach(subscription -> this.updateFeed(subscription));
        LOG.info("Finished updating feeds");
    }

    /**
     * Updates the given feed in a new transaction.
     *
     * @param subscription The feed to update
     * @throws FetcherServiceException when fetching the feed failed
     */
    private void updateFeed(final Subscription subscription) {

        final String url = subscription.getUrl();
        LOG.debug("Updating feed {}", url);

        try {

            this.transactionHelper.executeInNewTransaction(() -> {

                if (subscription != null) {

                    final SyndFeed syndFeed = this.feedService.fetch(url);
                    final Set<Entry> entries = this.feedService.getEntries(syndFeed);
                    final Collection<Entry> newEntries = this.extractNewEntries(subscription, entries);
                    newEntries.parallelStream().forEach(entry -> {
                        entry.setSubscription(subscription);
                    });
                    this.entryRepository.save(newEntries);

                }

                return null;

            });

            LOG.debug("Finished updating feed: {}", url);

        } catch (final Exception e) {

            LOG.info("An error occured while updating feed: {}", url);

        }

    }

    /**
     * Extracts new entries.
     *
     * @param subscription The subscription the entries should belong to
     * @param entries The entries to process
     * @return New entries
     */
    private Collection<Entry> extractNewEntries(final Subscription subscription, final Set<Entry> entries) {
        return entries.parallelStream().filter(entry -> {
            final BooleanBuilder filter = new BooleanBuilder();
            filter.and(QEntry.entry.subscription.eq(subscription));
            filter.and(QEntry.entry.uri.eq(entry.getUri()));
            return !this.entryRepository.exists(filter);
        }).collect(toList());
    }

}
