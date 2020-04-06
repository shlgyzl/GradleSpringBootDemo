package com.application.config;

import io.github.jhipster.config.liquibase.AsyncSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
public class LiquibaseConfiguration {
    private final Environment env;

    public LiquibaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean
    public SpringLiquibase liquibase(
            @Qualifier(value = "hikariDataSource") DataSource hikariDataSource,
            @Qualifier("taskExecutor") Executor taskExecutor,
            LiquibaseProperties liquibaseProperties) {

        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(taskExecutor, env);
        liquibase.setDataSource(hikariDataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
        liquibase.setShouldRun(true);
        // 不关闭数据源,否则会出现异常
        liquibase.setCloseDataSourceOnceMigrated(false);
        return liquibase;
    }
}
