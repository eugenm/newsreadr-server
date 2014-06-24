package de.patrickgotthard.newsreadr.server.subscriptions;

import de.patrickgotthard.newsreadr.server.common.repository.NewsreadrRepository;
import de.patrickgotthard.newsreadr.server.feeds.Feed;
import de.patrickgotthard.newsreadr.server.users.User;

public interface SubscriptionRepository extends NewsreadrRepository<Subscription> {

    long countByUserAndFeed(User user, Feed feed);

}
