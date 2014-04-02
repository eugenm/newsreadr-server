package de.patrickgotthard.newsreadr.server.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

public class HttpServletRequestUtilTest {

    @Test
    public void testIsApiRequestWithApiCall() {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/api?method=some_method");

        final boolean apiRequest = HttpServletRequestUtil.isApiRequest(request);
        assertThat(apiRequest, is(true));

    }

    @Test
    public void testIsApiRequestWithNormalCall() {

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/index");

        final boolean apiRequest = HttpServletRequestUtil.isApiRequest(request);
        assertThat(apiRequest, is(false));

    }

}
