package de.patrickgotthard.newsreadr.server.service;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.persistence.entity.AbstractUserEntity;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.security.NewsreadrUserDetails;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@TestExecutionListeners(TransactionalTestExecutionListener.class)
public class SecurityServiceTest {

    private static final String USERNAME = "username";
    private static final long USER_ID = 1;
    private static final String PASSWORD = "password";
    private static final Role ROLE = Role.ADMIN;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private SecurityService securityService;

    @Before
    public void setup() {

        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        securityService = new SecurityService(userRepository, passwordEncoder);

        final User user = new User.Builder().setId(USER_ID).setUsername(USERNAME).setPassword(PASSWORD).setRole(ROLE).build();

        final NewsreadrUserDetails userDetails = new NewsreadrUserDetails(user);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

    }

    @After
    public void cleanup() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
    }

    @Test
    @Transactional
    public void testGetCurrentUser() {

        final User user = new User();

        when(userRepository.findOne(USER_ID)).thenReturn(user);

        final User currentUser = securityService.getCurrentUser();
        assertThat(currentUser, is(notNullValue()));

    }

    @Test
    public void testGetCurrentUserId() {

        final User user = new User();

        when(userRepository.findOne(USER_ID)).thenReturn(user);

        final long currentUserId = securityService.getCurrentUserId();
        assertThat(currentUserId, is(USER_ID));

    }

    @Test
    public void testValidateOwnership() {

        final User user = spy(new User());
        when(user.getId()).thenReturn(USER_ID);

        final AbstractUserEntity userEntity = new AbstractUserEntity() {
        };
        userEntity.setUser(user);

        securityService.validateOwnership(userEntity);

    }

    @Test
    public void testIsCurrentUserAdmin() {
        final boolean currentUserAdmin = securityService.isCurrentUserAdmin();
        assertThat(currentUserAdmin, is(true));
    }

    @Test
    public void testEncode() {

        final String reverse = new StringBuilder(PASSWORD).reverse().toString();
        when(passwordEncoder.encode(PASSWORD)).thenReturn(reverse);

        final String encode = securityService.encode(PASSWORD);

        verify(passwordEncoder).encode(PASSWORD);
        assertThat(encode, is(reverse));

    }

}
