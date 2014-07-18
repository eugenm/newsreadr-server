package de.patrickgotthard.newsreadr.server.common.web;

import org.springframework.web.context.request.NativeWebRequest;

public final class RequestParameters {

    private RequestParameters() {
    }

    public static String getString(final NativeWebRequest request, final String paramName) {
        return request.getParameter(paramName);
    }

    public static Boolean getBoolean(final NativeWebRequest request, final String paramName) {
        Boolean value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Boolean.valueOf(parameter);
        }
        return value;
    }

    public static Integer getInteger(final NativeWebRequest request, final String paramName) {
        Integer value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Integer.valueOf(parameter);
        }
        return value;
    }

    public static Long getLong(final NativeWebRequest request, final String paramName) {
        Long value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Long.valueOf(parameter);
        }
        return value;
    }

    public static <T extends Enum<T>> T getEnum(final NativeWebRequest request, final String paramName, final Class<T> type) {
        final String stringValue = getString(request, paramName);
        T value = null;
        if (stringValue != null) {
            value = Enum.valueOf(type, stringValue);
        }
        return value;
    }

}
