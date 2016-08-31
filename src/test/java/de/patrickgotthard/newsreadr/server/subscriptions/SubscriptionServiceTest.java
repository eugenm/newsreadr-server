package de.patrickgotthard.newsreadr.server.subscriptions;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Predicate;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.tx.TransactionHelper;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.subscriptions.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.UpdateSubscriptionRequest;

public class SubscriptionServiceTest {

    private FeedRepository feedRepository;
    private EntryRepository entryRepository;
    private UserRepository userRepository;
    private UserEntryRepository userEntryRepository;
    private FolderRepository folderRepository;
    private SubscriptionRepository subscriptionRepository;
    private FeedService feedService;
    private SubscriptionService subscriptionService;
    private TransactionHelper transactionHelper;

    @Before
    public void setup() {
        this.feedRepository = mock(FeedRepository.class);
        this.entryRepository = mock(EntryRepository.class);
        this.userRepository = mock(UserRepository.class);
        this.userEntryRepository = mock(UserEntryRepository.class);
        this.folderRepository = mock(FolderRepository.class);
        this.subscriptionRepository = mock(SubscriptionRepository.class);
        this.feedService = mock(FeedService.class);
        this.transactionHelper = mock(TransactionHelper.class);

        this.subscriptionService = new SubscriptionService(this.feedRepository, this.entryRepository, this.userRepository, this.userEntryRepository,
                this.folderRepository, this.subscriptionRepository, this.feedService, this.transactionHelper);
    }

    @Test
    public void testAddSubscription() {

        final long folderId = 1l;
        final String url = "url";
        final String subscriptionTitle = "title";
        final Folder folder = new Folder();
        final Feed feed = new Feed();
        feed.setEntries(new HashSet<Entry>());

        when(this.folderRepository.findOne(any(Predicate.class))).thenReturn(folder);
        when(this.feedService.fetch(url)).thenReturn(feed);
        when(this.feedRepository.save(feed)).thenReturn(feed);

        final AddSubscriptionRequest request = new AddSubscriptionRequest();
        request.setUrl(url);
        request.setFolderId(folderId);
        request.setTitle(subscriptionTitle);

        final long currentUserId = 1L;
        this.subscriptionService.addSubscription(request, currentUserId);

    }

    @Test
    public void testUpdateSubscription() {

        final long subscriptionId = 1l;
        final String newTitle = "newTitle";
        final Folder folder = new Folder();

        final Subscription subscription = new Subscription();
        subscription.setFolder(folder);

        when(this.subscriptionRepository.findOne(any(Predicate.class))).thenReturn(subscription);

        final UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        request.setSubscriptionId(subscriptionId);
        request.setTitle(newTitle);

        final long currentUserId = 1L;
        this.subscriptionService.updateSubscription(request, currentUserId);

    }

    @Test
    public void testRemoveSubscription() {

        final long subscriptionId = 1l;
        final Subscription subscription = new Subscription();

        final RemoveSubscriptionRequest request = new RemoveSubscriptionRequest();
        request.setSubscriptionId(subscriptionId);

        when(this.subscriptionRepository.findOne(any(Predicate.class))).thenReturn(subscription);

        final long currentUserId = 1L;
        this.subscriptionService.removeSubscription(request, currentUserId);

    }

}
