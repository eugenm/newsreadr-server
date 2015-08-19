package de.patrickgotthard.newsreadr.server.common.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
class NewsreadrAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public NewsreadrAuthenticationEntryPoint() {
        super("/login");
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException e)
            throws IOException, ServletException {

        final String contextPath = request.getContextPath();
        final String requestURI = request.getRequestURI();
        final boolean isApiRequest = requestURI.startsWith(contextPath + "/api");

        if (isApiRequest) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(request, response, e);
        }

    }

}