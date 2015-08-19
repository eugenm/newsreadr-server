package de.patrickgotthard.newsreadr.server.users;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.users.request.AddUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.server.users.response.UserDTO;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Before
    public void setup() {
        this.userRepository = mock(UserRepository.class);
        this.passwordEncoder = mock(PasswordEncoder.class);
        this.userService = new UserService(this.userRepository, this.passwordEncoder);
    }

    @Test
    public void testAddUser() {

        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;
        final AddUserRequest request = new AddUserRequest.Builder().setUsername(username).setPassword(password).setRole(role).build();

        final User admin = new User();
        admin.setRole(Role.ADMIN);

        this.userService.addUser(request, admin);

    }

    @Test
    public void testGetUsers() {

        final User admin = new User();
        admin.setRole(Role.ADMIN);

        final List<UserDTO> users = this.userService.getUsers(admin);
        assertThat(users, is(notNullValue()));

    }

    @Test
    public void testUpdateUser() {

        final long userId = 1l;
        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;

        final User user = new User();

        final User currentUser = new User();
        currentUser.setId(userId);

        when(this.userRepository.findOne(1l)).thenReturn(user);

        final UpdateUserRequest request = new UpdateUserRequest.Builder().setUserId(userId).setUsername(username).setPassword(password).setRole(role).build();
        this.userService.updateUser(request, currentUser);

    }

    @Test
    public void testRemoveUser() {

        final long userId = 1l;
        final User user = new User();

        final User currentUser = new User();
        currentUser.setId(userId);

        final RemoveUserRequest request = new RemoveUserRequest();
        request.setUserId(userId);

        when(this.userRepository.findOne(1l)).thenReturn(user);

        this.userService.removeUser(request, currentUser);

    }

}
