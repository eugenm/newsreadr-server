package de.patrickgotthard.newsreadr.server.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.server.test.Request;
import de.patrickgotthard.newsreadr.server.users.response.UserDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class UserControllerIT {

    @Test
    public void testAddUser() {
        final ResponseEntity<String> response = Request.asAdmin().path("/users?username=newUser&password=password&role=USER").post();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testGetUsers() {

        final ResponseEntity<List<UserDTO>> response = Request.asAdmin().path("/users").get(new ParameterizedTypeReference<List<UserDTO>>() {
        });

        final UserDTO user = response.getBody().get(0);
        final UserDTO admin = response.getBody().get(1);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        assertThat(user.getUserId(), is(1l));
        assertThat(user.getUsername(), is("user"));
        assertThat(user.getRole(), is(Role.USER));

        assertThat(admin.getUserId(), is(2l));
        assertThat(admin.getUsername(), is("admin"));
        assertThat(admin.getRole(), is(Role.ADMIN));

    }

    @Test
    public void testUpdateUser() {
        final ResponseEntity<String> response = Request.asUser().path("/users/1?username=newUser&password=newUser&role=USER").put();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void testRemoveUser() {
        final ResponseEntity<String> response = Request.asUser().path("/users/1").delete();
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}
