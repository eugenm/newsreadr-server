package de.patrickgotthard.newsreadr.server.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1935166012295920105L;

    public AlreadyExistsException(final String message) {
        super(message);
    }

}
