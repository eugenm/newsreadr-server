package de.patrickgotthard.newsreadr.server.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.patrickgotthard.newsreadr.server.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.shared.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;

public class EntryServiceTest {

    private UserEntryRepository userEntryRepository;
    private SecurityService securityService;
    private EntryService entryService;

    @Before
    public void setup() {
        userEntryRepository = mock(UserEntryRepository.class);
        securityService = mock(SecurityService.class);
        entryService = new EntryService(userEntryRepository, securityService);
    }

    @Test
    public void testGetEntryContent() {

        final long userEntryId = 1l;
        final String content = "content";
        final Entry entry = new Entry.Builder().setContent(content).build();
        final Subscription subscription = new Subscription();
        final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).setEntry(entry).build();

        when(userEntryRepository.findOne(userEntryId)).thenReturn(userEntry);

        doNothing().when(securityService).validateOwnership(subscription);

        final GetEntryRequest request = new GetEntryRequest(userEntryId);
        final GetEntryResponse response = entryService.getEntry(request);

        assertThat(response.getContent(), is(content));

    }

}
