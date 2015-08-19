package de.patrickgotthard.newsreadr.server.entries;

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
public class EntryControllerIT {

    @Test
    public void testAddBookmark() {
        final ResponseEntity<String> response = Request.asUser().path("/entries/1/bookmark").post();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testRemoveBookmark() {
        final ResponseEntity<String> response = Request.asUser().path("/entries/2/bookmark").delete();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}
