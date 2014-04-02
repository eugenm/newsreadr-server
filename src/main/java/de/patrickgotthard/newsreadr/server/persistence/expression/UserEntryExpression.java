package de.patrickgotthard.newsreadr.server.persistence.expression;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.QUserEntry;

public final class UserEntryExpression {

    private UserEntryExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return QUserEntry.userEntry.subscription.user.id.eq(userId);
    }

    public static BooleanExpression belongsToSubscription(final long subscriptionId) {
        return QUserEntry.userEntry.subscription.id.eq(subscriptionId);
    }

    public static BooleanExpression belongsToFolder(final long folderId) {
        return QUserEntry.userEntry.subscription.folder.id.eq(folderId);
    }

    public static BooleanExpression idIsLowerThanOrEqualTo(final long id) {
        return QUserEntry.userEntry.id.loe(id);
    }

    public static BooleanExpression isBookmarked() {
        return QUserEntry.userEntry.bookmarked.isTrue();
    }

    public static BooleanExpression isUnread() {
        return QUserEntry.userEntry.read.isFalse();
    }

}
