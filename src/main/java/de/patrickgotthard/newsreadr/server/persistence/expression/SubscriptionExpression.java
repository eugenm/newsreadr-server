package de.patrickgotthard.newsreadr.server.persistence.expression;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.persistence.entity.QSubscription;

public final class SubscriptionExpression {

    private SubscriptionExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return QSubscription.subscription.user.id.eq(userId);
    }

    public static BooleanExpression referencesFeed(final Feed feed) {
        return QSubscription.subscription.feed.eq(feed);
    }

    public static BooleanExpression hasNoFolder() {
        return QSubscription.subscription.folder.isNull();
    }

}
