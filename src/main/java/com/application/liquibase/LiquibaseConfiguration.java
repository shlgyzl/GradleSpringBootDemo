package com.application.liquibase;

import io.github.jhipster.config.liquibase.AsyncSpringLiquibase;
import liquibase.integration.spring.SpringLiquibase;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.concurrent.Executor;

@Configuration
@EnableConfigurationProperties(LiquibaseProperties.class)
@AllArgsConstructor
public class LiquibaseConfiguration {
//    private final Environment env;
//    private final DataSource dataSource;
//    private final Executor executor;
//    private final LiquibaseProperties liquibaseProperties;
//
//    @Bean
//    public SpringLiquibase liquibase() {
//        AsyncSpringLiquibase liquibase = new AsyncSpringLiquibase(executor, env);
//        liquibase.setDataSource(dataSource);
//        liquibase.setChangeLog("classpath:config/liquibase/db.changelog-master.xml");
//        liquibase.setContexts(liquibaseProperties.getContexts());
//        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
//        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
//        liquibase.setChangeLogParameters(liquibaseProperties.getParameters());
//        liquibase.setShouldRun(true);
//        // 不关闭数据源,否则会出现异常
//        liquibase.setCloseDataSourceOnceMigrated(false);
//        return liquibase;
//    }
}
