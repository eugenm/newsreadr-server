package de.patrickgotthard.newsreadr.server.test;

import org.springframework.boot.test.TestRestTemplate;

public final class ControllerTestUtil {

    private static final String API_URL = "http://localhost:12345/api";
    private static final TestRestTemplate ADMIN_TEMPLATE = new TestRestTemplate("admin", "admin");
    private static final TestRestTemplate USER_TEMPLATE = new TestRestTemplate("user", "user");

    private ControllerTestUtil() {
    }

    public static <T> T getAsAdmin(final String url, final Class<T> responseType) {
        return ADMIN_TEMPLATE.getForObject(API_URL + url, responseType);
    }

    public static <T> T getAsUser(final String url, final Class<T> responseType) {
        return USER_TEMPLATE.getForObject(API_URL + url, responseType);
    }

}
