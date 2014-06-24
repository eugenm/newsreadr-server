package de.patrickgotthard.newsreadr.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import de.patrickgotthard.newsreadr.server.users.User;
import de.patrickgotthard.newsreadr.server.users.UserRepository;

@Component
public class NewsreadrUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public NewsreadrUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new NewsreadrUserDetails(user);

    }

}
