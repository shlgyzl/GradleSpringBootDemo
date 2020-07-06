package com.application.dynamic_data_source;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanghaiyong
 * 2020/7/4-8:13
 */
public class DynamicDataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static List<String> dataSourceIds = new ArrayList<>();

    //使用setDataSourceType设置当前的
    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    //判断指定DataSource当前是否存在
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
