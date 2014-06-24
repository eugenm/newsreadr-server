package de.patrickgotthard.newsreadr.server.common.util;

import java.util.Objects;

public final class Assert {

    private Assert() {
    }

    public static void notNull(final Object object) {
        org.springframework.util.Assert.notNull(object);
    }

    public static void equals(final Object firstObject, final Object secondObject) {
        final boolean equals = Objects.equals(firstObject, secondObject);
        isTrue(equals);
    }

    public static void isTrue(final boolean expression) {
        org.springframework.util.Assert.isTrue(expression);
    }

}
