package de.patrickgotthard.newsreadr.server.common.util;

public final class StringUtil {

    private StringUtil() {
    }

    public static boolean isBlank(final String string) {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

}
