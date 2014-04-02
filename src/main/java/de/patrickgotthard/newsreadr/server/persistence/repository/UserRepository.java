package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public interface UserRepository extends NewsreadrRepository<User> {

    User findByUsername(String username);

    long countByUsername(String username);

    long countByRole(Role role);

}
