package de.patrickgotthard.newsreadr.server.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import de.patrickgotthard.newsreadr.server.common.exception.ServiceException;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ControllerAdvice
class ExceptionControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String UNKNOWN_ERROR_MESSAGE = "An unknown error occured";

    @ExceptionHandler(Exception.class)
    @ResponseBody
    Response handleException(final HttpServletRequest request, final HttpServletResponse response, final Exception e) {
        String message;
        if (e instanceof ServiceException) {
            message = e.getMessage();
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("An error occured while processing request", e);
            message = UNKNOWN_ERROR_MESSAGE;
        }
        return Response.error(message);
    }

}
