package de.patrickgotthard.newsreadr.server.persistence.repository;

import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.repository.custom.CustomSubscriptionRepository;

public interface SubscriptionRepository extends NewsreadrRepository<Subscription>, CustomSubscriptionRepository {

    long countByUserAndFeed(User user, Feed feed);

}
