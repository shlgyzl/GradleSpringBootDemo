package com.application.dynamic_data_source;

import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        return  DynamicDataSourceContextHolder.getDataSourceType();
    }

    /**
     * 动态更新自定义数据源
     *
     * @param customDataSources 数据源
     */
    public void updateTargetDataSource(Map<String, DataSource> customDataSources) {
        Map<Object, Object> customDS = new HashMap<>(customDataSources);
        setTargetDataSources(customDS);
        afterPropertiesSet();
    }
}
