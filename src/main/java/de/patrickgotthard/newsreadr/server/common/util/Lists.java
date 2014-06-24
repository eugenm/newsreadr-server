package de.patrickgotthard.newsreadr.server.common.util;

import java.util.ArrayList;
import java.util.List;

public final class Lists {

    private Lists() {
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(final T... args) {
        final List<T> list = new ArrayList<>();
        for (final T arg : args) {
            list.add(arg);
        }
        return list;
    }

}
