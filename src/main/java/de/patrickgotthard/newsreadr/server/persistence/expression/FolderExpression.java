package de.patrickgotthard.newsreadr.server.persistence.expression;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.QFolder;

public final class FolderExpression {

    private FolderExpression() {
    }

    public static BooleanExpression belongsToUser(final long userId) {
        return QFolder.folder.user.id.eq(userId);
    }

}
