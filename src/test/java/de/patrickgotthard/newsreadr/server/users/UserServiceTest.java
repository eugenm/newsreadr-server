package de.patrickgotthard.newsreadr.server.users;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import de.patrickgotthard.newsreadr.server.security.SecurityService;
import de.patrickgotthard.newsreadr.server.users.User;
import de.patrickgotthard.newsreadr.server.users.UserRepository;
import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public class UserServiceTest {

    private UserRepository userRepository;
    private SecurityService securityService;
    private UserService userService;

    @Before
    public void setup() {
        userRepository = mock(UserRepository.class);
        securityService = mock(SecurityService.class);
        userService = new UserService(userRepository, securityService);
    }

    @Test
    public void testAddUser() {

        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;

        final AddUserRequest request = new AddUserRequest(username, password, role);
        userService.addUser(request);

    }

    @Test
    public void testGetUsers() {
        final GetUsersResponse response = userService.getUsers();
        assertThat(response.getSuccess(), is(true));
        assertThat(response.getUsers(), is(notNullValue()));
    }

    @Test
    public void testUpdateUser() {

        final long userId = 1l;
        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;
        final User user = new User();

        when(securityService.isCurrentUserAdmin()).thenReturn(true);
        when(userRepository.findOne(1l)).thenReturn(user);

        final UpdateUserRequest request = new UpdateUserRequest(userId, username, password, role);
        userService.updateUser(request);

    }

    @Test
    public void testRemoveUser() {

        final long userId = 1l;
        final User user = new User();

        when(securityService.isCurrentUserAdmin()).thenReturn(true);
        when(userRepository.findOne(1l)).thenReturn(user);

        userService.removeUser(new RemoveUserRequest(userId));

    }

}
