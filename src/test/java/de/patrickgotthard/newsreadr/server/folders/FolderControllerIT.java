package de.patrickgotthard.newsreadr.server.folders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class FolderControllerIT {

    @Test
    public void testAddFolder() {
        final ResponseEntity<String> response = Request.asUser().path("/folders?title=newFolder").post();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testUpdateFolder() {
        final ResponseEntity<String> response = Request.asUser().path("/folders/1?title=newFolder").put();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testRemoveFolder() {
        final ResponseEntity<String> response = Request.asUser().path("/folders/1").delete();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}
