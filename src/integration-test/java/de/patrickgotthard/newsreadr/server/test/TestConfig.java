package de.patrickgotthard.newsreadr.server.test;

import javax.sql.DataSource;

import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

import de.patrickgotthard.newsreadr.server.config.ApplicationConfig;
import de.patrickgotthard.newsreadr.server.config.PersistenceConfig;

@Configuration
@Import(ApplicationConfig.class)
public class TestConfig {

    /** Overrides the dataSource configured in {@link PersistenceConfig}. */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        final DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(new H2DataTypeFactory());
        return bean;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        final DatabaseDataSourceConnectionFactoryBean bean = new DatabaseDataSourceConnectionFactoryBean(dataSource());
        bean.setDatabaseConfig(dbUnitDatabaseConfig());
        return bean;
    }

}
