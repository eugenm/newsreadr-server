package de.patrickgotthard.newsreadr.server.common.config;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
class RestTemplateConfig {

    private static final int TEN_SECONDS = 10_000;

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {

        builder.setConnectTimeout(TEN_SECONDS);
        builder.setReadTimeout(TEN_SECONDS);

        final RestTemplate template = builder.build();
        template.setInterceptors(Collections.singletonList((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "newsreadr");
            return execution.execute(request, body);
        }));
        return template;

    }

}
