package de.patrickgotthard.newsreadr.server.folders;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;
import de.patrickgotthard.newsreadr.server.users.User;

public interface FolderRepository extends NewsreadrRepository<Folder> {

    long countByUserAndTitle(User user, String title);

    Folder findByUserAndTitle(User user, String title);

}
