package com.application.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Collections;

/**
 * 此配置只适用于小型项目(存于JVM中),中大型项目则需要第三方插件
 */
@Configuration
@EnableCaching(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)// 使用cglib代理和aspectj切入
public class SpringCacheConfiguration {

    @Primary
    @Bean(name = "concurrentMapCacheManager")
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager();
        concurrentMapCacheManager.setAllowNullValues(false);
        concurrentMapCacheManager.setCacheNames(Collections.singletonList("default"));
        return concurrentMapCacheManager;
    }


}
