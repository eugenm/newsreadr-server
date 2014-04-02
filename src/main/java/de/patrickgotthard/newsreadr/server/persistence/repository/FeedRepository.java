package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;

public interface FeedRepository extends NewsreadrRepository<Feed> {

    Feed findByUrl(String url);

}
