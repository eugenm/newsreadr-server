package de.patrickgotthard.newsreadr.server.folders;

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
public class FolderControllerIT {

    @Test
    public void testAddFolder() {
        final Response response = ControllerTestUtil.getAsUser("?method=add_folder&title=newFolder", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

    @Test
    public void testUpdateFolder() {
        final Response response = ControllerTestUtil.getAsUser("?method=update_folder&folderId=1&title=newFolder", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

    @Test
    public void testRemoveFolder() {
        final Response response = ControllerTestUtil.getAsUser("?method=remove_folder&folderId=1", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

}
