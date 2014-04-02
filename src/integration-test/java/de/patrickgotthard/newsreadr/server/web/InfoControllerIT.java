package de.patrickgotthard.newsreadr.server.web;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.shared.response.GetInfosResponse;

public class InfoControllerIT extends AbstractIT {

    @Test
    public void testGetInfos() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=get_infos");
        final ResultActions result = mvc.perform(request);
        final String json = result.andReturn().getResponse().getContentAsString();
        final GetInfosResponse response = new ObjectMapper().readValue(json, GetInfosResponse.class);

        assertThat(response.getUserId(), is(USER_USER_ID));
        assertThat(response.getUsername(), is(USER_USERNAME));
        assertTrue(response.getServerVersion().matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"));
        assertTrue(response.getApiVersion().matches("\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?"));

    }

}
