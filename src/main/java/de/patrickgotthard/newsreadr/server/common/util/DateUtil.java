package de.patrickgotthard.newsreadr.server.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        LocalDateTime localDateTime = null;
        if (date != null) {
            long time = date.getTime();
            final Instant instant = Instant.ofEpochMilli(time);
            ZoneId zoneId = ZoneId.systemDefault();
            localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        }
        return localDateTime;
    }

}
