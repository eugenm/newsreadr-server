package de.patrickgotthard.newsreadr.server.common.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateConfig {

    private static final int TEN_SECONDS = 10_000;

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {

        final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(TEN_SECONDS).setConnectionRequestTimeout(TEN_SECONDS)
                .setSocketTimeout(TEN_SECONDS).build();

        final CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);

        return builder.requestFactory(factory).build();

    }

}
