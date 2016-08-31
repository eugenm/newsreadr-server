package de.patrickgotthard.newsreadr.server.common.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private List<CustomArgumentResolver> argumentResolvers;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.addAll(this.argumentResolvers);
    }

}
