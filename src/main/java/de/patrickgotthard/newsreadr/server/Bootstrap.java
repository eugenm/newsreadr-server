package de.patrickgotthard.newsreadr.server;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Configuration
class Bootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private final String initialUsername = "admin";

    @Value("${random.value}")
    private String initialPassword;

    @PostConstruct
    public void bootstrap() {
        if (userRepository.count() == 0) {
            final User user = new User();
            user.setUsername(initialUsername);
            user.setPassword(passwordEncoder.encode(initialPassword));
            user.setRole(Role.ADMIN);
            userRepository.save(user);
            LOG.info("\n\nCreated admin user with password: {}\n", initialPassword);
        }
    }

}