package de.patrickgotthard.newsreadr.server.util;

import java.util.Objects;

public final class ObjectUtil {

    private ObjectUtil() {
    }

    public static boolean equals(final Object firstObject, final Object secondObject) {
        return Objects.equals(firstObject, secondObject);
    }

    public static boolean notEquals(final Object firstObject, final Object secondObject) {
        return !equals(firstObject, secondObject);
    }

}
