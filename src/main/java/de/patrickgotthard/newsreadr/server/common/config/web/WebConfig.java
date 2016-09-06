package de.patrickgotthard.newsreadr.server.common.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private List<CustomArgumentResolver> argumentResolvers;

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/entries/Entries").setViewName("entries/Entries");
        registry.addViewController("/login").setViewName("login/Login");
        registry.addViewController("/profile/Profile").setViewName("profile/Profile");
        registry.addViewController("/subscriptions/Subscriptions").setViewName("subscriptions/Subscriptions");
        registry.addViewController("/users/Users").setViewName("users/Users");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.addAll(this.argumentResolvers);
    }

}
