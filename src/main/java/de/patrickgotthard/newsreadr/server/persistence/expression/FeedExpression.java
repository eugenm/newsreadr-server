package de.patrickgotthard.newsreadr.server.persistence.expression;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.QFeed;

public final class FeedExpression {

    private static final QFeed FEED = QFeed.feed;

    private FeedExpression() {
    }

    public static BooleanExpression isUnused() {
        return FEED.subscriptions.isEmpty();
    }

}
