package de.patrickgotthard.newsreadr.server.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import de.patrickgotthard.newsreadr.server.common.web.CustomArgumentResolver;

@Configuration
class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private List<CustomArgumentResolver> customArgumentResolvers;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.addAll(customArgumentResolvers);
    }

    @Bean
    public AbstractHandlerMapping apiMethodHandlerMapping() {
        final ApiMethodHandlerMapping mapping = new ApiMethodHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return mapping;
    }

}