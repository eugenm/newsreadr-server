package de.patrickgotthard.newsreadr.server.users;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.dsl.BooleanExpression;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUser;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.rest.AlreadyExistsException;
import de.patrickgotthard.newsreadr.server.common.rest.NotAllowedException;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;
import de.patrickgotthard.newsreadr.server.common.util.StringUtil;
import de.patrickgotthard.newsreadr.server.users.request.AddUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.RemoveUserRequest;
import de.patrickgotthard.newsreadr.server.users.request.UpdateUserRequest;
import de.patrickgotthard.newsreadr.server.users.response.UserDTO;

@Service
class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void addUser(final AddUserRequest request, final long currentUserId) {

        LOG.debug("Adding user: {}", request);

        final User currentUser = this.userRepository.findOne(currentUserId);
        if (!Role.ADMIN.equals(currentUser.getRole())) {
            throw new NotAllowedException("You are not allowed to create users");
        }

        final String username = request.getUsername();
        final BooleanExpression filter = QUser.user.username.eq(username);
        final boolean userAlreadyExists = this.userRepository.exists(filter);

        if (userAlreadyExists) {

            throw new AlreadyExistsException("User " + username + " already exists");

        } else {

            final String password = this.passwordEncoder.encode(request.getPassword());
            final Role role = request.getRole();

            final User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            this.userRepository.save(user);

            LOG.debug("Successfully added user: {}", request);

        }

    }

    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(final long currentUserId) {

        final User currentUser = this.userRepository.findOne(currentUserId);
        if (!Role.ADMIN.equals(currentUser.getRole())) {
            throw new NotAllowedException("You are not allowed to view the user list");
        }

        final List<UserDTO> users = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> {

            final Long userId = user.getId();
            final String username = user.getUsername();
            final Role role = user.getRole();

            final UserDTO dto = new UserDTO();
            dto.setUserId(userId);
            dto.setUsername(username);
            dto.setRole(role);

            users.add(dto);

        });
        return users;

    }

    @Transactional
    public void updateUser(final UpdateUserRequest request, final long currentUserId) {

        LOG.debug("Updating user: {}", request);

        final User currentUser = this.userRepository.findOne(currentUserId);

        final long userId = request.getUserId();
        final boolean changingOwnAccount = userId == currentUserId;

        // normal users can only update their own account
        if (!Role.ADMIN.equals(currentUser.getRole()) && !changingOwnAccount) {
            throw new NotAllowedException("You are not allowed to update account of other users");
        }

        // load user
        final User user = this.userRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }

        // check if new username is already in use
        final String oldUsername = user.getUsername();
        final String newUsername = request.getUsername();
        if (StringUtil.isNotBlank(newUsername) && ObjectUtil.notEqual(oldUsername, newUsername)) {
            final BooleanExpression byUsername = QUser.user.username.eq(newUsername);
            final boolean usernameReserved = this.userRepository.count(byUsername) == 1;
            if (usernameReserved) {
                throw new AlreadyExistsException("User " + newUsername + " does already exist");
            }
        }

        final Role oldRole = user.getRole();
        final Role newRole = request.getRole();
        final boolean degradeAdmin = Role.ADMIN.equals(oldRole) && !Role.ADMIN.equals(newRole);

        if (degradeAdmin) {
            // ensure that there is always an admin left
            final BooleanExpression admins = QUser.user.role.eq(Role.ADMIN);
            final boolean lastAdmin = this.userRepository.count(admins) == 1;
            if (lastAdmin) {
                throw new NotAllowedException("Not allowed to degrade the last admin");
            }
        }

        if (StringUtil.isNotBlank(newUsername)) {
            user.setUsername(newUsername);
        }

        final String newPassword = request.getPassword();
        if (StringUtil.isNotBlank(newPassword)) {
            final String password = this.passwordEncoder.encode(newPassword);
            user.setPassword(password);
        }

        if (newRole != null) {
            user.setRole(newRole);
        }

        this.userRepository.save(user);

        if (changingOwnAccount) {
            SecurityContextHolder.clearContext();
        }

        LOG.debug("Successfully updated user: {}", request);

    }

    @Transactional
    public void removeUser(final RemoveUserRequest request, final long currentUserId) {

        LOG.debug("Removing user: {}", request);

        final User currentUser = this.userRepository.findOne(currentUserId);

        final long userId = request.getUserId();

        if (!Role.ADMIN.equals(currentUser.getRole())) {
            // normal users can only delete their own account
            if (currentUserId != userId) {
                throw new NotAllowedException("You are not allowed to remove other users");
            }
        }

        final User user = this.userRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }

        if (Role.ADMIN.equals(user.getRole())) {
            // ensure that there is always an admin left
            final BooleanExpression admins = QUser.user.role.eq(Role.ADMIN);
            final boolean lastAdmin = this.userRepository.count(admins) == 1;
            if (lastAdmin) {
                throw new NotAllowedException("Not allowed to delete the last admin");
            }
        }

        this.userRepository.delete(userId);

        LOG.debug("Successfully removed user: {}", request);

    }

}
