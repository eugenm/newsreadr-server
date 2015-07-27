package de.patrickgotthard.newsreadr.server.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowedException extends RuntimeException {

    private static final long serialVersionUID = -8565740165885501061L;

    public NotAllowedException(final String message) {
        super(message);
    }

}
