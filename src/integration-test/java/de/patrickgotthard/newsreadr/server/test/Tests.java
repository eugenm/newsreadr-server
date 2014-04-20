package de.patrickgotthard.newsreadr.server.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Tests {

    private static final ObjectMapper jsonMapper = new ObjectMapper();

    public static final long USER_USER_ID = 1;
    public static final long ADMIN_USER_ID = 2;

    public static final String USER_USERNAME = "user";
    public static final String ADMIN_USERNAME = "admin";
    public static final String UNKNOWN_USERNAME = "unknown";

    public static final String USER_PASSWORD = "$2a$10$nt0ls1ZI48ncyNi44jwp7.ZX2lkbZR.h9rOF2DSZq.gqYvXlPq0U.";
    public static final String ADMIN_PASSWORD = "$2a$10$Oy7eCE8k68EOMXpSbWYNiufkQmTCgpglzF.cSVFFUe2Na6RNEx4M.";

    public static final String ADMIN_BASE_AUTH = "Basic YWRtaW46YWRtaW4=";
    public static final String USER_BASE_AUTH = "Basic dXNlcjp1c2Vy";

    private Tests() {
    }

    public static void assertResponse(final ResultActions result, final Object value) {
        try {
            final String expected = jsonMapper.writeValueAsString(value);
            final String actual = result.andReturn().getResponse().getContentAsString();
            assertThat(actual, is(expected));
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readJson(final String json, final Class<T> targetType) {
        try {
            return jsonMapper.readValue(json, targetType);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static MockHttpServletRequestBuilder getAsAdmin(final String url) {
        return get(url).header("Authorization", ADMIN_BASE_AUTH);
    }

    public static MockHttpServletRequestBuilder getAsUser(final String url) {
        return get(url).header("Authorization", USER_BASE_AUTH);
    }

    private static MockHttpServletRequestBuilder get(final String url) {
        return MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON);
    }

}
