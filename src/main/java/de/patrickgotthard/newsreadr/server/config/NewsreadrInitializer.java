package de.patrickgotthard.newsreadr.server.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class NewsreadrInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {

        // set session timeout to one hour
        final SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
        sessionCookieConfig.setMaxAge(3600);

        // application context
        final AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ApplicationConfig.class);

        final ContextLoaderListener applicationContextLoaderListener = new ContextLoaderListener(applicationContext);
        servletContext.addListener(applicationContextLoaderListener);

        // dispatcher context
        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(MvcConfig.class);

        // dispatcher servlet
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);
        final ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // register security filter
        final DelegatingFilterProxy securityFilter = new DelegatingFilterProxy();
        final FilterRegistration.Dynamic securityFilterMapping = servletContext.addFilter("springSecurityFilterChain", securityFilter);
        securityFilterMapping.addMappingForUrlPatterns(null, false, "/*");

        // encoding filter
        final CharacterEncodingFilter utf8Filter = new CharacterEncodingFilter();
        utf8Filter.setEncoding("UTF-8");
        utf8Filter.setForceEncoding(true);
        final FilterRegistration.Dynamic encodingFilterMapping = servletContext.addFilter("UTF8Filter", utf8Filter);
        encodingFilterMapping.addMappingForUrlPatterns(null, false, "/*");

    }

}