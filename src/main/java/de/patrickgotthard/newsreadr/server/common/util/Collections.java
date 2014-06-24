package de.patrickgotthard.newsreadr.server.common.util;

import java.util.Collection;

public final class Collections {

    private Collections() {
    }

    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

}
