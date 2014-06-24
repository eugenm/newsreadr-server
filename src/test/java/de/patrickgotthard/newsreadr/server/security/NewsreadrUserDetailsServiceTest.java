package de.patrickgotthard.newsreadr.server.security;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.patrickgotthard.newsreadr.server.users.User;
import de.patrickgotthard.newsreadr.server.users.UserRepository;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public class NewsreadrUserDetailsServiceTest {

    private static final long USER_ID = 1l;
    private static final String EXISTING_USERNAME = "username";
    private static final String UNKNOW_USERNAME = "unknown";

    private UserRepository userRepository;
    private NewsreadrUserDetailsService service;

    @Before
    public void setup() {

        final User user = new User.Builder().setId(USER_ID).setUsername(EXISTING_USERNAME).setRole(Role.USER).build();

        userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(EXISTING_USERNAME)).thenReturn(user);
        when(userRepository.findByUsername(UNKNOW_USERNAME)).thenReturn(null);

        service = new NewsreadrUserDetailsService(userRepository);

    }

    @Test
    public void testLoadExistingUserByUsername() {
        final UserDetails userDetails = service.loadUserByUsername(EXISTING_USERNAME);
        assertThat(userDetails, instanceOf(NewsreadrUserDetails.class));

        final NewsreadrUserDetails casted = (NewsreadrUserDetails) userDetails;
        assertThat(casted.getUserId(), is(USER_ID));
        assertThat(casted.getUsername(), is(EXISTING_USERNAME));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUnknownUserByUsername() {
        service.loadUserByUsername(UNKNOW_USERNAME);
    }

}
