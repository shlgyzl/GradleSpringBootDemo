package com.application.design_pattern.proxy_design_pattern;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MetricsCollectorImpl implements MetricsCollector<RequestInfo> {
    @Override
    public void recordRequest(RequestInfo requestInfo) {
        log.info("代理模式打印日志信息:{}", requestInfo.toString());
    }
}
