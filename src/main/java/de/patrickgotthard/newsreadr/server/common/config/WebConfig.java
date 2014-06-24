package de.patrickgotthard.newsreadr.server.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.patrickgotthard.newsreadr.server.common.CustomArgumentResolver;

@Configuration
class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private List<CustomArgumentResolver> customArgumentResolvers;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.addAll(customArgumentResolvers);
    }

}