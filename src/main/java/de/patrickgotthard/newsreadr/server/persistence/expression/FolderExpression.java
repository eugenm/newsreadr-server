package de.patrickgotthard.newsreadr.server.persistence.expression;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.QFolder;

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
