package de.patrickgotthard.newsreadr.server.common.persistence.repository;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;

public interface UserRepository extends NewsreadrRepository<User> {

    User findByUsername(String username);

}
