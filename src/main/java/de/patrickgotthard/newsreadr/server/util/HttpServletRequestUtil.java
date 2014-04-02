package de.patrickgotthard.newsreadr.server.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpServletRequestUtil {

    private HttpServletRequestUtil() {
    }

    public static boolean isApiRequest(final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String requestURI = request.getRequestURI();
        return requestURI.startsWith(contextPath + "/api");
    }

}