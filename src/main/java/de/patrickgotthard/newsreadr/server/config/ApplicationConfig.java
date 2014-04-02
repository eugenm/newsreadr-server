package de.patrickgotthard.newsreadr.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:///${user.home}/.newsreadr/newsreadr.properties")
@Import({ PersistenceConfig.class, ServiceConfig.class, SchedulerConfig.class, SecurityConfig.class })
public class ApplicationConfig {

}
