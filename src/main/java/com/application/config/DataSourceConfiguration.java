package com.application.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

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

    //@Bean(name = "hikariDataSource")// 配置HikariDataSource数据源
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl()); //数据源
        config.setUsername(dataSourceProperties.getUsername()); //用户名
        config.setPassword(dataSourceProperties.getPassword()); //密码
        config.setDriverClassName(dataSourceProperties.getDriverClassName());
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.setInitializationFailTimeout(10000);
        config.setConnectionInitSql("select 1");
        return new HikariDataSource(config);
    }

    @Bean(name = "druidDataSource")// 自定义DruidDataSource数据源
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        // 校验数据库是否可用
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setValidationQuery("SELECT 1");
        return druidDataSource;
    }

    @Bean(name = "basicDataSource")
    public DataSource basicDataSource() {
        return dataSourceBuilder.type(BasicDataSource.class).build();
    }

    @Bean(name = "basic2DataSource")
    public DataSource basic2DataSource() {
        return dataSourceBuilder.type(org.apache.commons.dbcp2.BasicDataSource.class).build();
    }

    @Bean(name = "jdbcDataSource")
    public DataSource jdbcDataSource() {
        return dataSourceBuilder.type(org.apache.tomcat.jdbc.pool.DataSource.class).build();
    }

    /**
     * 创建嵌入式数据源
     *
     * @return DataSource
     */
    @Bean(name = "hSQLDataSource")
    public DataSource hSQLDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();
    }
}
