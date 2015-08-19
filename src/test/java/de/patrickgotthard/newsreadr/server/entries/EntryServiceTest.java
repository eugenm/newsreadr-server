package de.patrickgotthard.newsreadr.server.entries;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.server.entries.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.server.entries.request.RemoveBookmarkRequest;

public class EntryServiceTest {

    private EntryDAO entryDAO;
    private UserEntryRepository userEntryRepository;
    private EntryService entryService;

    @Before
    public void setup() {
        this.userEntryRepository = mock(UserEntryRepository.class);
        this.entryDAO = mock(EntryDAO.class);
        this.entryService = new EntryService(this.userEntryRepository, this.entryDAO);
    }

    @Test
    public void testGetEntryContent() {

        final long userEntryId = 1l;
        final String content = "content";
        final Entry entry = new Entry.Builder().setContent(content).build();
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).setEntry(entry).build();

        final GetEntryRequest request = new GetEntryRequest();
        request.setUserEntryId(userEntryId);

        when(this.userEntryRepository.findOne(any(Predicate.class))).thenReturn(userEntry);

        final User user = new User();
        user.setId(1l);

        final String response = this.entryService.getEntry(request, user);
        assertThat(response, is(content));

    }

    @Test
    public void testAddBookmark() {

        final long userEntryId = 1l;
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).build();

        final AddBookmarkRequest request = new AddBookmarkRequest();
        request.setUserEntryId(userEntryId);

        when(this.userEntryRepository.findOne(any(Predicate.class))).thenReturn(userEntry);

        this.entryService.addBookmark(request, new User());

        final ArgumentCaptor<UserEntry> captor = ArgumentCaptor.forClass(UserEntry.class);
        verify(this.userEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getBookmarked(), is(true));
    }

    @Test
    public void testRemoveBookmark() {

        final long userEntryId = 1l;
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).build();

        final RemoveBookmarkRequest request = new RemoveBookmarkRequest();
        request.setUserEntryId(userEntryId);

        when(this.userEntryRepository.findOne(any(Predicate.class))).thenReturn(userEntry);

        this.entryService.removeBookmark(request, new User());

        final ArgumentCaptor<UserEntry> captor = ArgumentCaptor.forClass(UserEntry.class);
        verify(this.userEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getBookmarked(), is(false));

    }

}
