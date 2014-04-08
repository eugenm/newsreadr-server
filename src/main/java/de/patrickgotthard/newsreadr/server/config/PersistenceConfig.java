package de.patrickgotthard.newsreadr.server.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import liquibase.integration.spring.SpringLiquibase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories("de.patrickgotthard.newsreadr.server.persistence.repository")
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class PersistenceConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {

        final String url = env.getProperty("mysql.url");
        final String username = env.getProperty("mysql.username");
        final String password = env.getProperty("mysql.password");

        final HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(MysqlDataSource.class.getName());
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);

        return new HikariDataSource(config);

    }

    @Bean
    public SpringLiquibase liquibase() {
        final SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:liquibase/changelog-master.xml");
        return liquibase;
    }

    @Bean
    @DependsOn("liquibase")
    public EntityManagerFactory entityManagerFactory() {

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        final Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.format_sql", "true");

        final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource());
        factory.setPackagesToScan("de.patrickgotthard.newsreadr.server.persistence.entity");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaProperties(jpaProperties);
        factory.afterPropertiesSet();
        return factory.getObject();

    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory());
    }

}
