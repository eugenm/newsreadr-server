package de.patrickgotthard.newsreadr.server.web.resolver;

import org.springframework.web.context.request.NativeWebRequest;

final class ParameterResolver {

    private final NativeWebRequest request;

    ParameterResolver(final NativeWebRequest request) {
        this.request = request;
    }

    String getString(final String paramName) {
        return request.getParameter(paramName);
    }

    Boolean getBoolean(final String paramName) {
        Boolean value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Boolean.valueOf(parameter);
        }
        return value;
    }

    Integer getInteger(final String paramName) {
        Integer value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Integer.valueOf(parameter);
        }
        return value;
    }

    Long getLong(final String paramName) {
        Long value = null;
        final String parameter = request.getParameter(paramName);
        if (parameter != null) {
            value = Long.valueOf(parameter);
        }
        return value;
    }

    <T extends Enum<T>> T getEnum(final String paramName, final Class<T> type) {
        final String stringValue = getString(paramName);
        T value = null;
        if (stringValue != null) {
            value = Enum.valueOf(type, stringValue);
        }
        return value;
    }

}
