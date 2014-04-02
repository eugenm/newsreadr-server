package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;

public interface FolderRepository extends NewsreadrRepository<Folder> {

    long countByUserAndTitle(User user, String title);

    Folder findByUserAndTitle(User user, String title);

}
