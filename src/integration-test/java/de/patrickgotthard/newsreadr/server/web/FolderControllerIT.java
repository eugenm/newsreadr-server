package de.patrickgotthard.newsreadr.server.web;

import static de.patrickgotthard.newsreadr.server.test.Tests.assertResponse;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.shared.response.Response;

public class FolderControllerIT extends AbstractIT {

    @Test
    public void testAddFolder() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=add_folder&title=newFolder");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

    @Test
    public void testUpdateFolder() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=update_folder&folderId=1&title=newFolder");
        final ResultActions resultActions = mvc.perform(request);
        assertResponse(resultActions, Response.success());
    }

    @Test
    public void testRemoveFolder() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=remove_folder&folderId=1");
        final ResultActions resultActions = mvc.perform(request);
        assertResponse(resultActions, Response.success());
    }

}
