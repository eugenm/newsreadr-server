package de.patrickgotthard.newsreadr.server.subscriptions;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUser;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

@Repository
class SubscriptionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Node> getSubscriptions(final long currentUserId) {

        final QSubscription subscription = QSubscription.subscription;
        final QUser user = subscription.user;
        final QEntry entry = QEntry.entry;

        final NumberPath<Long> id = subscription.id;
        final StringPath title = subscription.title;
        final StringPath url = subscription.url;
        final NumberExpression<Long> unread = entry.id.count();

        // @formatter:off
        return new JPAQuery<>(this.entityManager)
            .select(id, title, unread, url)
            .from(subscription)
            .leftJoin(subscription.entries, entry).on(entry.read.isFalse())
            .where(user.id.eq(currentUserId))
            .groupBy(id)
            .fetch()
            .parallelStream()
            .map(tuple -> {
                final Node node = new Node();
                node.setType(Type.SUBSCRIPTION);
                node.setId(tuple.get(id));
                node.setTitle(tuple.get(title));
                node.setUrl(tuple.get(url));
                node.setUnread(tuple.get(unread));
                return node;
            })
            .collect(toList());
        // @formatter:on

    }

}
