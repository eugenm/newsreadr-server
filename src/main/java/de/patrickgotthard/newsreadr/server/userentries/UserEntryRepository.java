package de.patrickgotthard.newsreadr.server.userentries;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;

public interface UserEntryRepository extends NewsreadrRepository<UserEntry>, CustomUserEntryRepository {

}
