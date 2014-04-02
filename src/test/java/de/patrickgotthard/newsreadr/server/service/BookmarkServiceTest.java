package de.patrickgotthard.newsreadr.server.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.shared.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveBookmarkRequest;

public class BookmarkServiceTest {

    private UserEntryRepository userEntryRepository;
    private SecurityService securityService;
    private BookmarkService bookmarkService;

    @Before
    public void setup() {
        userEntryRepository = mock(UserEntryRepository.class);
        securityService = mock(SecurityService.class);
        bookmarkService = new BookmarkService(userEntryRepository, securityService);
    }

    @Test
    public void testAddBookmark() {

        final long userEntryId = 1l;
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).build();

        when(userEntryRepository.findOne(userEntryId)).thenReturn(userEntry);
        doNothing().when(securityService).validateOwnership(subscription);

        final AddBookmarkRequest request = new AddBookmarkRequest(userEntryId);
        bookmarkService.addBookmark(request);

        final ArgumentCaptor<UserEntry> captor = ArgumentCaptor.forClass(UserEntry.class);
        verify(userEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getBookmarked(), is(true));
    }

    @Test
    public void testRemoveBookmark() {

        final long userEntryId = 1l;
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).build();

        when(userEntryRepository.findOne(userEntryId)).thenReturn(userEntry);
        doNothing().when(securityService).validateOwnership(subscription);

        final RemoveBookmarkRequest request = new RemoveBookmarkRequest(userEntryId);
        bookmarkService.removeBookmark(request);

        final ArgumentCaptor<UserEntry> captor = ArgumentCaptor.forClass(UserEntry.class);
        verify(userEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getBookmarked(), is(false));

    }

}
