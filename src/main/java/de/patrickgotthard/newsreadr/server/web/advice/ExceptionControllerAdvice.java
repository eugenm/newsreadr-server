package de.patrickgotthard.newsreadr.server.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import de.patrickgotthard.newsreadr.server.service.data.ServiceException;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String UNKNOWN_ERROR_MESSAGE = "An unknown error occured";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(final HttpServletRequest request, final Exception e) {
        String message;
        if (e instanceof ServiceException) {
            message = e.getMessage();
        } else {
            LOG.error("An error occured while processing request", e);
            message = UNKNOWN_ERROR_MESSAGE;
        }
        return Response.error(message);
    }

}
