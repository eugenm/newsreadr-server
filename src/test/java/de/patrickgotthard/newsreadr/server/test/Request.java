package de.patrickgotthard.newsreadr.server.test;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class Request {

    private static final String API_URL = "http://localhost:12345/api";

    private final TestRestTemplate template;
    private String path;

    private Request(final TestRestTemplate template) {
        this.template = template;
    }

    public static Request asAdmin() {
        final TestRestTemplate template = new TestRestTemplate("admin", "admin");
        return new Request(template);
    }

    public static Request asUser() {
        final TestRestTemplate template = new TestRestTemplate("user", "user");
        return new Request(template);
    }

    public Request path(final String path) {
        this.path = API_URL + path;
        return this;
    }

    public <T> ResponseEntity<T> get(final Class<T> responseType) {
        return this.execute(HttpMethod.GET, responseType);
    }

    public <T> ResponseEntity<T> get(final ParameterizedTypeReference<T> responseType) {
        return this.execute(HttpMethod.GET, responseType);
    }

    public ResponseEntity<String> post() {
        return this.execute(HttpMethod.POST);
    }

    public ResponseEntity<String> put() {
        return this.execute(HttpMethod.PUT);
    }

    public ResponseEntity<String> delete() {
        return this.execute(HttpMethod.DELETE);
    }

    private ResponseEntity<String> execute(final HttpMethod method) {
        return this.template.exchange(this.path, method, null, String.class);
    }

    private <T> ResponseEntity<T> execute(final HttpMethod method, final Class<T> responseType) {
        return this.template.exchange(this.path, method, null, responseType);
    }

    private <T> ResponseEntity<T> execute(final HttpMethod method, final ParameterizedTypeReference<T> responseType) {
        return this.template.exchange(this.path, method, null, responseType);
    }

}
