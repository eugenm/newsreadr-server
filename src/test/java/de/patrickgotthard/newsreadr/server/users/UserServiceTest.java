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

        final long userId = 1L;
        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;

        final User admin = new User();
        admin.setId(userId + 1);
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setRole(Role.ADMIN);

        when(this.userRepository.findOne(userId)).thenReturn(admin);

        final AddUserRequest request = new AddUserRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setRole(role);

        this.userService.addUser(request, userId);

    }

    @Test
    public void testGetUsers() {

        final long userId = 1L;

        final User user = new User();
        user.setId(userId);
        user.setRole(Role.ADMIN);

        when(this.userRepository.findOne(userId)).thenReturn(user);

        final List<UserDTO> users = this.userService.getUsers(userId);
        assertThat(users, is(notNullValue()));
    }

    @Test
    public void testUpdateUser() {

        final long userId = 1L;
        final String username = "username";
        final String password = "password";
        final Role role = Role.USER;

        final User user = new User();
        user.setId(userId);
        user.setRole(Role.ADMIN);

        when(this.userRepository.findOne(userId)).thenReturn(user);

        final UpdateUserRequest request = new UpdateUserRequest();
        request.setUserId(userId);
        request.setUsername(username);
        request.setPassword(password);
        request.setRole(role);

        this.userService.updateUser(request, userId);

    }

    @Test
    public void testRemoveUser() {

        final long userId = 1L;
        final User user = new User();

        final RemoveUserRequest request = new RemoveUserRequest();
        request.setUserId(userId);

        when(this.userRepository.findOne(userId)).thenReturn(user);

        this.userService.removeUser(request, userId);

    }

}
