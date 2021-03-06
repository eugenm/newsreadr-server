package de.patrickgotthard.newsreadr.server.common.util;

import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectUtil {

    private ObjectUtil() {
    }

    public static boolean equals(final Object o1, final Object o2) {
        return Objects.equals(o1, o2);
    }

    public static boolean notEqual(final Object o1, final Object o2) {
        return !equals(o1, o2);
    }

    public static String toString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
