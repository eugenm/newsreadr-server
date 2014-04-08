package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;

public interface SubscriptionRepository extends NewsreadrRepository<Subscription> {

    long countByUserAndFeed(User user, Feed feed);

}
