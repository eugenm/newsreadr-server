package de.patrickgotthard.newsreadr.server.web;

import static de.patrickgotthard.newsreadr.server.test.Tests.assertResponse;
import static de.patrickgotthard.newsreadr.server.test.Tests.getAsUser;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.shared.response.Response;

public class BookmarkControllerIT extends AbstractIT {

    @Test
    public void testAddBookmark() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=add_bookmark&userEntryId=1");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

    @Test
    public void testRemoveBookmark() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=remove_bookmark&userEntryId=2");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

}
