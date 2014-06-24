package de.patrickgotthard.newsreadr.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.common.entity.AbstractUserEntity;
import de.patrickgotthard.newsreadr.server.common.util.Assert;
import de.patrickgotthard.newsreadr.server.common.util.Objects;
import de.patrickgotthard.newsreadr.server.users.User;
import de.patrickgotthard.newsreadr.server.users.UserRepository;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(final UserRepository userRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User getCurrentUser() {
        final long currentUserId = getCurrentUserId();
        final User currentUser = userRepository.findOne(currentUserId);
        Assert.notNull(currentUser);
        return currentUser;
    }

    public long getCurrentUserId() {
        return getUserDetails().getUserId();
    }

    public String getCurrentUsername() {
        return getUserDetails().getUsername();
    }

    public Role getCurrentUserRole() {
        return getUserDetails().getRole();
    }

    public boolean notBelongsToUser(final AbstractUserEntity entity) {
        return Objects.notEquals(getCurrentUserId(), entity.getUser().getId());
    }

    public void validateOwnership(final AbstractUserEntity entity) {
        Assert.notNull(entity);
        Assert.equals(getCurrentUserId(), entity.getUser().getId());
    }

    public boolean isCurrentUserNoAdmin() {
        return Objects.notEquals(Role.ADMIN, getCurrentUserRole());
    }

    public boolean isCurrentUserAdmin() {
        return Objects.equals(Role.ADMIN, getCurrentUserRole());
    }

    public String encode(final String password) {
        return passwordEncoder.encode(password);
    }

    private NewsreadrUserDetails getUserDetails() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();
        final Object principal = authentication.getPrincipal();
        final NewsreadrUserDetails userDetails = (NewsreadrUserDetails) principal;
        return userDetails;
    }

}
