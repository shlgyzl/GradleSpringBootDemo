package com.application.design_pattern.proxy_design_pattern;

/**
 * 记录类
 */
public interface MetricsCollector<T extends RequestInfo> {
    /**
     * 日志信息
     */
    default void recordRequest(T t) {
    }
}
