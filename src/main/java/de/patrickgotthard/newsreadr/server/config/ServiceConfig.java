package de.patrickgotthard.newsreadr.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:info.properties")
@ComponentScan("de.patrickgotthard.newsreadr.server.service")
public class ServiceConfig {

}
