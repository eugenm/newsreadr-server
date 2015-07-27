package de.patrickgotthard.newsreadr.server.common.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;

@Configuration
class Bootstrap {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void bootstrap() {

        if (this.userRepository.count() == 0) {

            final String initialUsername = "admin";
            final String initialPassword = "password";

            final User user = new User();
            user.setUsername(initialUsername);
            user.setPassword(this.passwordEncoder.encode(initialPassword));
            user.setRole(Role.ADMIN);
            this.userRepository.save(user);

        }

    }

}