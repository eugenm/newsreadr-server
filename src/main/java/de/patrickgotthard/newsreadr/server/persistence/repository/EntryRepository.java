package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;

public interface EntryRepository extends NewsreadrRepository<Entry> {

    long countByFeedAndUri(Feed feed, String uri);

}
