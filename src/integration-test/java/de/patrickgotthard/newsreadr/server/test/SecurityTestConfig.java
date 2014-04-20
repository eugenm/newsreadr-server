package de.patrickgotthard.newsreadr.server.test;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.patrickgotthard.newsreadr.server.config.SecurityConfig;

@Configuration
@Import(SecurityConfig.class)
public class SecurityTestConfig {

}
