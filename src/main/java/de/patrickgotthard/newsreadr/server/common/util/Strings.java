package de.patrickgotthard.newsreadr.server.common.util;

import org.apache.commons.lang3.StringUtils;

public final class Strings {

    private Strings() {
    }

    public static boolean isBlank(final String string) {
        return StringUtils.isBlank(string);
    }

    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }

    public static boolean equals(final String string1, final String string2) {
        return StringUtils.equals(string1, string2);
    }

    public static boolean notEquals(final String s1, final String s2) {
        return !equals(s1, s2);
    }

    public static String cleanUrl(final String url) {
        final String trimmed = StringUtils.trim(url);
        return StringUtils.removeEnd(trimmed, "/");
    }

}
