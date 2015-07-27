package de.patrickgotthard.newsreadr.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
