package com.application.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * 简单缓存
 */
@Configuration
@Slf4j
public class SpringSimpleCacheConfiguration {

//    @Bean(name = "simpleCacheManager")
//    public CacheManager simpleCacheManager() {
//        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
//        //simpleCacheManager.setCaches(Collections.singletonList(new ConcurrentMapCache("simpleCache")));
//        log.info("创建简单缓存管理器--->[simpleCacheManager]");
//        return simpleCacheManager;
//    }
}
