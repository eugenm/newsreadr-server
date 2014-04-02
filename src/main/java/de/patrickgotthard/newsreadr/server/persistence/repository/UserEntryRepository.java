package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.persistence.repository.custom.CustomUserEntryRepository;

public interface UserEntryRepository extends NewsreadrRepository<UserEntry>, CustomUserEntryRepository {

}
