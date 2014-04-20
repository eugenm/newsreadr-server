package de.patrickgotthard.newsreadr.server.web;

import static de.patrickgotthard.newsreadr.server.test.Tests.assertResponse;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;

public class EntryControllerIT extends AbstractIT {

    @Test
    public void testGetEntry() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=get_entry&userEntryId=1");
        final ResultActions result = mvc.perform(request);
        final GetEntryResponse expected = new GetEntryResponse("content1");
        assertResponse(result, expected);
    }

}
