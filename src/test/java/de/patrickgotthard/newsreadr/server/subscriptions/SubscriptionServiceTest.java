package de.patrickgotthard.newsreadr.server.subscriptions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import de.patrickgotthard.newsreadr.server.entries.Entry;
import de.patrickgotthard.newsreadr.server.entries.EntryRepository;
import de.patrickgotthard.newsreadr.server.feeds.Feed;
import de.patrickgotthard.newsreadr.server.feeds.FeedRepository;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.folders.Folder;
import de.patrickgotthard.newsreadr.server.folders.FolderRepository;
import de.patrickgotthard.newsreadr.server.security.SecurityService;
import de.patrickgotthard.newsreadr.server.userentries.UserEntryRepository;
import de.patrickgotthard.newsreadr.shared.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;

public class SubscriptionServiceTest {

    private FeedRepository feedRepository;
    private EntryRepository entryRepository;
    private UserEntryRepository userEntryRepository;
    private FolderRepository folderRepository;
    private SubscriptionRepository subscriptionRepository;
    private FeedService feedService;
    private SecurityService securityService;
    private SubscriptionService subscriptionService;

    @Before
    public void setup() {
        feedRepository = mock(FeedRepository.class);
        entryRepository = mock(EntryRepository.class);
        userEntryRepository = mock(UserEntryRepository.class);
        folderRepository = mock(FolderRepository.class);
        subscriptionRepository = mock(SubscriptionRepository.class);
        feedService = mock(FeedService.class);
        securityService = mock(SecurityService.class);
        subscriptionService = new SubscriptionService(feedRepository, entryRepository, userEntryRepository, folderRepository, subscriptionRepository,
                feedService, securityService);
    }

    @Test
    public void testAddSubscription() {

        final long folderId = 1l;
        final String url = "url";
        final String subscriptionTitle = "title";
        final Folder folder = new Folder();
        final Feed feed = new Feed();
        feed.setEntries(new HashSet<Entry>());

        when(folderRepository.findOne(folderId)).thenReturn(folder);
        when(feedService.fetch(url)).thenReturn(feed);
        when(securityService.notBelongsToUser(folder)).thenReturn(false);
        when(feedRepository.save(feed)).thenReturn(feed);

        final AddSubscriptionRequest request = new AddSubscriptionRequest(url, folderId, subscriptionTitle);
        subscriptionService.addSubscription(request);

    }

    @Test
    public void testUpdateSubscription() {

        final long subscriptionId = 1l;
        final String newTitle = "newTitle";
        final Folder folder = new Folder();
        final Subscription subscription = new Subscription.Builder().setFolder(folder).build();

        when(subscriptionRepository.findOne(subscriptionId)).thenReturn(subscription);
        when(securityService.notBelongsToUser(folder)).thenReturn(false);
        when(securityService.notBelongsToUser(subscription)).thenReturn(false);

        final UpdateSubscriptionRequest request = new UpdateSubscriptionRequest(subscriptionId, null, newTitle);
        subscriptionService.updateSubscription(request);

    }

    @Test
    public void testRemoveSubscription() {

        final long subscriptionId = 1l;
        final Subscription subscription = new Subscription();

        when(subscriptionRepository.findOne(subscriptionId)).thenReturn(subscription);
        when(securityService.notBelongsToUser(subscription)).thenReturn(false);

        final RemoveSubscriptionRequest request = new RemoveSubscriptionRequest(subscriptionId);
        subscriptionService.removeSubscription(request);

    }

}
