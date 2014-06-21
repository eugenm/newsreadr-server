package de.patrickgotthard.newsreadr.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.patrickgotthard.newsreadr.server.web.resolver.CustomArgumentResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private List<CustomArgumentResolver> customArgumentResolvers;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.addAll(customArgumentResolvers);
    }

    // @Override
    // public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    // registry.addResourceHandler("/css/**").addResourceLocations("/css/");
    // registry.addResourceHandler("/js/**").addResourceLocations("/js/");
    // registry.addResourceHandler("/lib/**").addResourceLocations("/lib/");
    // registry.addResourceHandler("/partial/**").addResourceLocations("/templates/partial/");
    // registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
    // }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}