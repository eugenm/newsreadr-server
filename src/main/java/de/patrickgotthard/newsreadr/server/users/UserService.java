package de.patrickgotthard.newsreadr.server.users;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.common.exception.ServiceException;
import de.patrickgotthard.newsreadr.server.common.util.Objects;
import de.patrickgotthard.newsreadr.server.common.util.Strings;
import de.patrickgotthard.newsreadr.server.security.SecurityService;
import de.patrickgotthard.newsreadr.shared.request.AddUserRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.shared.response.GetUsersResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;
import de.patrickgotthard.newsreadr.shared.response.data.Role;
import de.patrickgotthard.newsreadr.shared.response.data.UserData;

@Service
class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Autowired
    UserService(final UserRepository userRepository, final SecurityService securityService) {
        this.userRepository = userRepository;
        this.securityService = securityService;
    }

    @Secured("ROLE_ADMIN")
    @Transactional
    Response addUser(final AddUserRequest request) {

        LOG.debug("Adding user: {}", request);

        final String username = request.getUsername();
        final boolean userAlreadyExists = userRepository.countByUsername(username) != 0;

        if (userAlreadyExists) {

            throw ServiceException.withMessage("User '{}' already exists", username);

        } else {

            final String password = securityService.encode(request.getPassword());
            final Role role = request.getRole();
            final User user = new User.Builder().setUsername(username).setPassword(password).setRole(role).build();
            userRepository.save(user);

            LOG.debug("Successfully added user: {}", request);
            return Response.success();

        }

    }

    @Secured("ROLE_ADMIN")
    @Transactional(readOnly = true)
    GetUsersResponse getUsers() {
        final List<User> users = userRepository.findAll();
        final List<UserData> userList = new ArrayList<>();
        for (final User user : users) {
            final Long userId = user.getId();
            final String username = user.getUsername();
            final Role role = user.getRole();
            userList.add(new UserData(userId, username, role));
        }
        return new GetUsersResponse(userList);
    }

    @Transactional
    Response updateUser(final UpdateUserRequest request) {

        LOG.debug("Updating user: {}", request);

        final long userId = request.getUserId();
        final long currentUserId = securityService.getCurrentUserId();

        final boolean changingOwnAccount = Objects.equals(userId, currentUserId);

        // normal users can only update their own account
        if (securityService.isCurrentUserNoAdmin() && !changingOwnAccount) {
            throw ServiceException.withMessage("You are not allowed to edit accounts of other users");
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw ServiceException.withMessage("User does not exist");
        }

        final String oldUsername = user.getUsername();
        final String newUsername = request.getUsername();
        if (Strings.isNotBlank(newUsername) && Strings.notEquals(oldUsername, newUsername)) {
            final boolean usernameReserved = userRepository.countByUsername(newUsername) == 1;
            if (usernameReserved) {
                throw ServiceException.withMessage("Username '{}' already exists", newUsername);
            }
        }

        final Role oldRole = user.getRole();
        final Role newRole = request.getRole();
        final boolean degradeAdmin = Objects.equals(Role.ADMIN, oldRole) && Objects.equals(Role.USER, newRole);

        if (degradeAdmin) {
            // ensure that there is always an admin left
            final boolean lastAdmin = userRepository.countByRole(Role.ADMIN) == 1;
            if (lastAdmin) {
                throw ServiceException.withMessage("It is not allowed to degrade the last administrator");
            }
        }

        if (Strings.isNotBlank(newUsername)) {
            user.setUsername(newUsername);
        }

        final String newPassword = request.getPassword();
        if (Strings.isNotBlank(newPassword)) {
            final String password = securityService.encode(newPassword);
            user.setPassword(password);
        }

        if (newRole != null) {
            user.setRole(newRole);
        }

        userRepository.save(user);

        if (changingOwnAccount) {
            SecurityContextHolder.clearContext();
        }

        LOG.debug("Successfully updated user: {}", request);
        return Response.success();

    }

    @Transactional
    Response removeUser(final RemoveUserRequest request) {

        LOG.debug("Removing user: {}", request);

        final long userId = request.getUserId();

        if (securityService.isCurrentUserNoAdmin()) {
            // normal users can only delete their own account
            final long currentUserId = securityService.getCurrentUserId();
            if (Objects.notEquals(currentUserId, userId)) {
                throw ServiceException.withMessage("You are not allowed to delete accounts of other users");
            }
        }

        final User user = userRepository.findOne(userId);
        if (user == null) {
            throw ServiceException.withMessage("User does not exist");
        }

        if (Objects.equals(Role.ADMIN, user.getRole())) {
            // ensure that there is always an admin left
            final boolean lastAdmin = userRepository.countByRole(Role.ADMIN) == 1;
            if (lastAdmin) {
                throw ServiceException.withMessage("The last administrator can't be removed");
            }
        }

        userRepository.delete(userId);

        LOG.debug("Successfully deleted last admin: {}", request);
        return Response.success();

    }

}