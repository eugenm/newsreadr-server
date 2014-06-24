package de.patrickgotthard.newsreadr.server.folders;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

public final class FolderExpression {

    private static final QFolder FOLDER = QFolder.folder;

    private FolderExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return FOLDER.user.id.eq(userId);
    }

    public static OrderSpecifier<String> orderByTitleAsc() {
        return FOLDER.title.asc();
    }

}
