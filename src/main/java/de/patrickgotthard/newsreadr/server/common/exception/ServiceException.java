package de.patrickgotthard.newsreadr.server.common.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 7362265851301369811L;

    private ServiceException(final String message) {
        super(message);
    }

    private ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static ServiceException withMessage(final String message) {
        return new ServiceException(message);
    }

    public static ServiceException withMessage(final String format, final Object... params) {
        final String message = String.format(format.replace("{}", "%s"), params);
        return new ServiceException(message);
    }

    public static ServiceException withCauseAndMessage(final Throwable cause, final String message) {
        return new ServiceException(message, cause);
    }

    public static ServiceException withCauseAndMessage(final Throwable cause, final String format, final Object... params) {
        final String message = String.format(format.replace("{}", "%s"), params);
        return new ServiceException(message, cause);
    }

}