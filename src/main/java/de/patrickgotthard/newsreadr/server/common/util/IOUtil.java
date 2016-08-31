package de.patrickgotthard.newsreadr.server.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

public final class IOUtil {

    private IOUtil() {
    }

    public static void write(final String data, final OutputStream output) throws IOException {
        IOUtils.write(data, output, StandardCharsets.UTF_8);
    }

}
