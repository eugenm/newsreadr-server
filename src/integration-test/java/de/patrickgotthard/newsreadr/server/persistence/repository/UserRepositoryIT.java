package de.patrickgotthard.newsreadr.server.persistence.repository;

import static de.patrickgotthard.newsreadr.server.test.TestData.ADMIN_PASSWORD;
import static de.patrickgotthard.newsreadr.server.test.TestData.ADMIN_USERNAME;
import static de.patrickgotthard.newsreadr.server.test.TestData.ADMIN_USER_ID;
import static de.patrickgotthard.newsreadr.server.test.TestData.UNKNOWN_USERNAME;
import static de.patrickgotthard.newsreadr.server.test.TestData.USER_PASSWORD;
import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USERNAME;
import static de.patrickgotthard.newsreadr.server.test.TestData.USER_USER_ID;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.test.IntegrationTest;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@RunWith(SpringJUnit4ClassRunner.class)
@IntegrationTest
@Transactional
public class UserRepositoryIT {

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
