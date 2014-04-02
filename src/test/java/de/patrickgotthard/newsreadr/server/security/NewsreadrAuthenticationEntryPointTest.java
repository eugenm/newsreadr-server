package de.patrickgotthard.newsreadr.server.security;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

public class NewsreadrAuthenticationEntryPointTest {

    @Test
    public void testCommenceWithDefaultCall() throws IOException, ServletException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/index");

        final HttpServletResponse response = mock(HttpServletResponse.class);

        final AuthenticationException authException = mock(AuthenticationException.class);

        final NewsreadrAuthenticationEntryPoint authenticationEntryPoint = spy(new NewsreadrAuthenticationEntryPoint());
        doNothing().when((LoginUrlAuthenticationEntryPoint) authenticationEntryPoint).commence(request, response, authException);
        authenticationEntryPoint.commence(request, response, authException);

        verify((LoginUrlAuthenticationEntryPoint) authenticationEntryPoint).commence(request, response, authException);

    }

    @Test
    public void testCommenceWithApiCall() throws IOException, ServletException {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/api?method=some_method");

        final HttpServletResponse response = mock(HttpServletResponse.class);

        final NewsreadrAuthenticationEntryPoint authenticationEntryPoint = new NewsreadrAuthenticationEntryPoint();
        authenticationEntryPoint.commence(request, response, null);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);

    }

}
