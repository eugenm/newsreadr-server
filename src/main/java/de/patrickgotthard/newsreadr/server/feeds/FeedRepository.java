package de.patrickgotthard.newsreadr.server.feeds;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;

public interface FeedRepository extends NewsreadrRepository<Feed> {

    Feed findByUrl(String url);

}
