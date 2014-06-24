package de.patrickgotthard.newsreadr.server.common.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpServletRequests {

    private HttpServletRequests() {
    }

    public static boolean isApiRequest(final HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        final String requestURI = request.getRequestURI();
        return requestURI.startsWith(contextPath + "/api");
    }

}