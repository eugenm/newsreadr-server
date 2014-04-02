package de.patrickgotthard.newsreadr.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("de.patrickgotthard.newsreadr.server.scheduler")
@EnableScheduling
public class SchedulerConfig {

}
