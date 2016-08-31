package de.patrickgotthard.newsreadr.server.common.config.security;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.patrickgotthard.newsreadr.server.common.rest.NewsreadrUserDetails;
import de.patrickgotthard.newsreadr.server.test.IntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
public class NewsreadrUserDetailsServiceIT {

    @Autowired
    private NewsreadrUserDetailsService service;

    @Test
    public void testLoadAdmin() {

        final long adminId = 2l;
        final String adminUsername = "admin";

        final UserDetails userDetails = this.service.loadUserByUsername(adminUsername);
        assertThat(userDetails, instanceOf(NewsreadrUserDetails.class));

        final NewsreadrUserDetails user = (NewsreadrUserDetails) userDetails;
        assertThat(user.getId(), is(adminId));
        assertThat(user.getUsername(), is(adminUsername));

    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUnknownUser() {
        final String unknownUser = "unknown";
        this.service.loadUserByUsername(unknownUser);
    }

}
