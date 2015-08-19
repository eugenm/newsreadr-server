package de.patrickgotthard.newsreadr.server.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUser;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;

@Component
class NewsreadrUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public NewsreadrUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Predicate withUsername = QUser.user.username.eq(username);
        final User user = this.userRepository.findOne(withUsername);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        return user;
    }

}
