package com.application.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfiguration {
    private final DataSourceProperties dataSourceProperties;
    private static DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

    @Autowired
    public DataSourceConfiguration(DataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
        dataSourceBuilder.url(dataSourceProperties.getUrl());
        dataSourceBuilder.password(dataSourceProperties.getPassword());
        dataSourceBuilder.username(dataSourceProperties.getUsername());
        dataSourceBuilder.driverClassName(dataSourceProperties.getDriverClassName());
        dataSourceBuilder.type(dataSourceProperties.getType());
    }

    @Primary
    @Bean(name = "hikariDataSource")// 配置HikariDataSource数据源
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl()); //数据源
        config.setUsername(dataSourceProperties.getUsername()); //用户名
        config.setPassword(dataSourceProperties.getPassword()); //密码
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        return new HikariDataSource(config);
    }

    @Bean(name = "mybatisDataSource")// 配置HikariDataSource数据源
    public DataSource mybatisDataSource() {
        HikariConfig config = new HikariConfig();
        // 手动指定数据源
        config.setJdbcUrl("jdbc:mysql://47.105.46.4:3306/mybatis?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC"); //数据源
        config.setUsername(dataSourceProperties.getUsername()); //用户名
        config.setPassword(dataSourceProperties.getPassword()); //密码
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        return new HikariDataSource(config);
    }

}
