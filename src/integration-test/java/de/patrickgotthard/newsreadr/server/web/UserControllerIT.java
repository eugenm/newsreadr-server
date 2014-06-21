package de.patrickgotthard.newsreadr.server.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.ControllerTestUtil;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;
import de.patrickgotthard.newsreadr.shared.response.data.Role;
import de.patrickgotthard.newsreadr.shared.response.data.UserData;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class UserControllerIT {

    @Test
    public void testAddUser() {
        final Response response = ControllerTestUtil.getAsAdmin("?method=add_user&username=newUser&password=password&role=USER", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

    @Test
    public void testGetUsers() {

        final GetUsersResponse response = ControllerTestUtil.getAsAdmin("?method=get_users", GetUsersResponse.class);
        final UserData user = response.getUsers().get(0);
        final UserData admin = response.getUsers().get(1);

        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));

        assertThat(user.getUserId(), is(1l));
        assertThat(user.getUsername(), is("user"));
        assertThat(user.getRole(), is(Role.USER));

        assertThat(admin.getUserId(), is(2l));
        assertThat(admin.getUsername(), is("admin"));
        assertThat(admin.getRole(), is(Role.ADMIN));

    }

    @Test
    public void testUpdateUser() {
        final Response response = ControllerTestUtil.getAsUser("?method=update_user&userId=1&username=newUser&password=newUser&role=USER", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

    @Test
    public void testRemoveUser() {
        final Response response = ControllerTestUtil.getAsUser("?method=remove_user&userId=1", Response.class);
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getMessage(), is(nullValue()));
    }

}
