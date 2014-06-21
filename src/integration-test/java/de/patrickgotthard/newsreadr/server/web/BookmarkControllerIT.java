package de.patrickgotthard.newsreadr.server.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.ControllerTestUtil;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class BookmarkControllerIT {

    protected static final String API_URL = "http://localhost:8080/api";

    @Test
    public void testAddBookmark() {
        final Response response = ControllerTestUtil.getAsUser("?method=add_bookmark&userEntryId=1", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

    @Test
    public void testRemoveBookmark() {
        final Response response = ControllerTestUtil.getAsUser("?method=remove_bookmark&userEntryId=2", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

}
