package de.patrickgotthard.newsreadr.server.common.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.rest.NewsreadrUserDetails;

@Component
class NewsreadrUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public NewsreadrUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
        return new NewsreadrUserDetails(user);
    }

}
