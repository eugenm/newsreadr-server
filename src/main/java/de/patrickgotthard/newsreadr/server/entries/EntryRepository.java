package de.patrickgotthard.newsreadr.server.entries;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;
import de.patrickgotthard.newsreadr.server.feeds.Feed;

public interface EntryRepository extends NewsreadrRepository<Entry> {

    long countByFeedAndUri(Feed feed, String uri);

}
