package de.patrickgotthard.newsreadr.server.infos;

import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USERNAME;
import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USER_ID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.infos.response.GetInfosResponse;
import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.Request;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class InfoControllerIT {

    @Test
    public void testGetInfos() {

        final ResponseEntity<GetInfosResponse> response = Request.asUser().path("/infos").get(GetInfosResponse.class);
        final GetInfosResponse infos = response.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(infos.getUserId(), is(USER_USER_ID));
        assertThat(infos.getUsername(), is(USER_USERNAME));

    }

}
