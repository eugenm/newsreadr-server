package de.patrickgotthard.newsreadr.server.persistence.repository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.test.AbstractIT;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public class UserRepositoryIT extends AbstractIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {

        final User user = userRepository.findByUsername(USER_USERNAME);
        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(USER_USER_ID));
        assertThat(user.getUsername(), is(USER_USERNAME));
        assertThat(user.getPassword(), is(USER_PASSWORD));
        assertThat(user.getRole(), is(Role.USER));

        final User admin = userRepository.findByUsername(ADMIN_USERNAME);
        assertThat(admin, is(notNullValue()));
        assertThat(admin.getId(), is(ADMIN_USER_ID));
        assertThat(admin.getUsername(), is(ADMIN_USERNAME));
        assertThat(admin.getPassword(), is(ADMIN_PASSWORD));
        assertThat(admin.getRole(), is(Role.ADMIN));

        final User unknown = userRepository.findByUsername(UNKNOWN_USERNAME);
        assertThat(unknown, is(nullValue()));

    }

    @Test
    public void testCountByUsername() {
        assertThat(userRepository.countByUsername(UNKNOWN_USERNAME), is(0l));
        assertThat(userRepository.countByUsername(USER_USERNAME), is(1l));
        assertThat(userRepository.countByUsername(ADMIN_USERNAME), is(1l));
    }

    @Test
    public void testCountByRole() {
        assertThat(userRepository.countByRole(Role.USER), is(1l));
        assertThat(userRepository.countByRole(Role.ADMIN), is(1l));
        assertThat(userRepository.countByRole(null), is(0l));
    }

}
