package de.patrickgotthard.newsreadr.server.userentries;

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
    public void testGetEntry() {
        final ResponseEntity<String> response = Request.asUser().path("/entries/1").get(String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("content1"));
    }

}
