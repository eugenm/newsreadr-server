package de.patrickgotthard.newsreadr.server.persistence.repository.custom;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.persistence.entity.QFeed;
import de.patrickgotthard.newsreadr.server.persistence.entity.QFolder;
import de.patrickgotthard.newsreadr.server.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;

@Repository
public class SubscriptionRepositoryImpl implements CustomSubscriptionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Folder> getSubscriptionsWithFolder(final Predicate predicate) {

        final QFolder qFolder = QFolder.folder;
        final QSubscription qSubscription = QSubscription.subscription;
        final QFeed qFeed = QFeed.feed;

        // @formatter:off
        return new JPAQuery(entityManager)
        .from(qFolder)
        .leftJoin(qFolder.subscriptions, qSubscription).fetchAll()
        .leftJoin(qSubscription.feed, qFeed).fetchAll()
        .where(predicate)
        .groupBy(qFolder.id)
        .list(qFolder);
        // @formatter:on

    }

    @Override
    public List<Subscription> getSubscriptionsWithoutFolder(final Predicate predicate) {

        final QSubscription qSubscription = QSubscription.subscription;
        final QFeed qFeed = QFeed.feed;

        // @formatter:off
        return new JPAQuery(entityManager)
        .from(qSubscription)
        .join(qSubscription.feed, qFeed).fetch()
        .where(predicate)
        .orderBy(qSubscription.title.asc())
        .list(qSubscription);
        // @formatter:on

    }

}
