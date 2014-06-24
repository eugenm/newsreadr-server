package de.patrickgotthard.newsreadr.server.subscriptions;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.feeds.Feed;

public final class SubscriptionExpression {

    private static final QSubscription SUBSCRIPTION = QSubscription.subscription;

    private SubscriptionExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return SUBSCRIPTION.user.id.eq(userId);
    }

    public static BooleanExpression belongsToFolder(final long folderId) {
        return SUBSCRIPTION.folder.id.eq(folderId);
    }

    public static BooleanExpression referencesFeed(final Feed feed) {
        return SUBSCRIPTION.feed.eq(feed);
    }

    public static BooleanExpression hasNoFolder() {
        return SUBSCRIPTION.folder.isNull();
    }

    public static OrderSpecifier<String> orderByTitleAsc() {
        return SUBSCRIPTION.title.asc();
    }

}
