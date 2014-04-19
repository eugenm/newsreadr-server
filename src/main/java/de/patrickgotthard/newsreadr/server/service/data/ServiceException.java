package de.patrickgotthard.newsreadr.server.service.data;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 7362265851301369811L;

    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final String format, final Object... params) {
        super(String.format(format.replace("{}", "%s"), params));
    }

}