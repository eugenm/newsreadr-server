package de.patrickgotthard.newsreadr.server.common.util;

public final class Objects {

    private Objects() {
    }

    public static boolean equals(final Object firstObject, final Object secondObject) {
        return java.util.Objects.equals(firstObject, secondObject);
    }

    public static boolean notEquals(final Object firstObject, final Object secondObject) {
        return !equals(firstObject, secondObject);
    }

}
