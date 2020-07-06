package com.application.jpa.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//@Import(DynamicDataSourceRegister.class) 如果开启动态数据源则开发此处
@AllArgsConstructor
public class DataSourceConfiguration {

//    private final DataSourceProperties dataSourceProperties;
//
//    @Bean
//    public DataSource dataSource() {
//        return dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//    }
}
