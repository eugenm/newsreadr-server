package de.patrickgotthard.newsreadr.server.feeds;

import com.mysema.query.types.expr.BooleanExpression;

public final class FeedExpression {

    private static final QFeed FEED = QFeed.feed;

    private FeedExpression() {
    }

    public static BooleanExpression isUnused() {
        return FEED.subscriptions.isEmpty();
    }

}
