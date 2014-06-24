package de.patrickgotthard.newsreadr.server.users;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public interface UserRepository extends NewsreadrRepository<User> {

    User findByUsername(String username);

    long countByUsername(String username);

    long countByRole(Role role);

}
