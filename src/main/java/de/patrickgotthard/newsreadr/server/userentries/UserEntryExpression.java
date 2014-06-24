package de.patrickgotthard.newsreadr.server.userentries;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.userentries.QUserEntry;

public final class UserEntryExpression {

    private static final QUserEntry USER_ENTRY = QUserEntry.userEntry;

    private UserEntryExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return USER_ENTRY.subscription.user.id.eq(userId);
    }

    public static BooleanExpression belongsToSubscription(final long subscriptionId) {
        return USER_ENTRY.subscription.id.eq(subscriptionId);
    }

    public static BooleanExpression belongsToFolder(final long folderId) {
        return USER_ENTRY.subscription.folder.id.eq(folderId);
    }

    public static BooleanExpression idIsLowerThanOrEqualTo(final long id) {
        return USER_ENTRY.id.loe(id);
    }

    public static BooleanExpression isBookmarked() {
        return USER_ENTRY.bookmarked.isTrue();
    }

    public static BooleanExpression isUnread() {
        return USER_ENTRY.read.isFalse();
    }

}
