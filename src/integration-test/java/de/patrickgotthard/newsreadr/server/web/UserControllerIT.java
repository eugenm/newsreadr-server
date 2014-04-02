package de.patrickgotthard.newsreadr.server.web;

import java.util.List;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.server.util.ListUtil;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;
import de.patrickgotthard.newsreadr.shared.response.data.Role;
import de.patrickgotthard.newsreadr.shared.response.data.UserData;

public class UserControllerIT extends AbstractIT {

    @Test
    public void testAddUser() throws Exception {
        final MockHttpServletRequestBuilder request = getAsAdmin("/api?method=add_user&username=newUser&password=password&role=USER");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

    @Test
    public void testGetUsers() throws Exception {
        final MockHttpServletRequestBuilder request = getAsAdmin("/api?method=get_users");
        final ResultActions result = mvc.perform(request);
        final UserData user = new UserData(1l, "user", Role.USER);
        final UserData admin = new UserData(2l, "admin", Role.ADMIN);
        final List<UserData> userList = ListUtil.toList(user, admin);
        final GetUsersResponse expected = new GetUsersResponse(userList);
        assertResponse(result, expected);
    }

    @Test
    public void testUpdateUser() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=update_user&userId=1&username=newUser&password=newUser&role=USER");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

    @Test
    public void testRemoveUser() throws Exception {
        final MockHttpServletRequestBuilder request = getAsUser("/api?method=remove_user&userId=1");
        final ResultActions result = mvc.perform(request);
        assertResponse(result, Response.success());
    }

}
