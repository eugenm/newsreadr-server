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

    public static String cleanUrl(final String url) {
        String cleaned = url == null ? "" : url;
        cleaned = cleaned.trim();
        if (cleaned.endsWith("/")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1);
        }
        return cleaned;
    }

}
