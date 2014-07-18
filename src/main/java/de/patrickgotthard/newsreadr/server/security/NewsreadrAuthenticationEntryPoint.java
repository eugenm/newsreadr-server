package de.patrickgotthard.newsreadr.server.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import de.patrickgotthard.newsreadr.server.common.web.HttpServletRequests;

@Component
public class NewsreadrAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    public NewsreadrAuthenticationEntryPoint() {
        super("/login");
    }

    @Override
    public void commence(final HttpServletRequest req, final HttpServletResponse res, final AuthenticationException ex) throws IOException, ServletException {
        if (HttpServletRequests.isApiRequest(req)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(req, res, ex);
        }
    }

}