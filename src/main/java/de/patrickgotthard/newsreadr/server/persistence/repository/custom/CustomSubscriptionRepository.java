package de.patrickgotthard.newsreadr.server.persistence.repository.custom;

import java.util.List;

import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;

public interface CustomSubscriptionRepository {

    List<Folder> getSubscriptionsWithFolder(Predicate predicate);

    List<Subscription> getSubscriptionsWithoutFolder(Predicate predicate);

}