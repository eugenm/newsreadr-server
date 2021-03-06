package de.patrickgotthard.newsreadr.server.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServerException(final String message) {
        super(message);
    }

    public ServerException(final Throwable cause) {
        super(cause);
    }

    public ServerException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
