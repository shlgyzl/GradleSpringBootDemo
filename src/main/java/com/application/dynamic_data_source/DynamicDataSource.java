package com.application.dynamic_data_source;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * 动态数据源
 *
 * @author yanghaiyong
 * 2020/6/29-0:15
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 用于静态数据源的获取
     *
     * @return Object
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }

    /**
     * 用于运行时动态创建数据源
     *
     * @return DataSource
     */
    @Override
    protected DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }
}
