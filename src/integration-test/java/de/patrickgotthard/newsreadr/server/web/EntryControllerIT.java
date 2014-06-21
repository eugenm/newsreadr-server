package de.patrickgotthard.newsreadr.server.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.ControllerTestUtil;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class EntryControllerIT {

    @Test
    public void testGetEntry() {
        final GetEntryResponse response = ControllerTestUtil.getAsUser("?method=get_entry&userEntryId=1", GetEntryResponse.class);
        assertThat(response.getContent(), is("content1"));
    }

}
