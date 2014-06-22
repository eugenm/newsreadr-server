package de.patrickgotthard.newsreadr.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Configuration
class Bootstrap {

    private static final String INITIAL_USERNAME = "admin";
    private static final String INITIAL_PASSWORD = "password";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void bootstrap() {
        if (userRepository.count() == 0) {
            final User user = new User();
            user.setUsername(INITIAL_USERNAME);
            user.setPassword(passwordEncoder.encode(INITIAL_PASSWORD));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }

}