package de.patrickgotthard.newsreadr.server.web;

import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USERNAME;
import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USER_ID;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.ControllerTestUtil;
import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class InfoControllerIT {

    @Test
    public void testGetInfos() {
        final GetInfosResponse response = ControllerTestUtil.getAsUser("?method=get_infos", GetInfosResponse.class);
        assertThat(response.getUserId(), is(USER_USER_ID));
        assertThat(response.getUsername(), is(USER_USERNAME));
        assertTrue(response.getServerVersion().matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"));
        assertTrue(response.getApiVersion().matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"));
    }

}
