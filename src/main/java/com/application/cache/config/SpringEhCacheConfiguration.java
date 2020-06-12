package com.application.cache.config;

import io.github.jhipster.config.JHipsterProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@Slf4j
@AllArgsConstructor
public class SpringEhCacheConfiguration {
    private final JHipsterProperties jHipsterProperties;

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        // 增强Ehcache功能,没啥用
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();
        javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                        .build());

        // 在此处添加需要cache的实体
        return cm -> {
            cm.createCache("eHcache", jcacheConfiguration);
        };
    }


    @Bean(name = "simpleCacheManager")
    public CacheManager simpleCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("simpleCache")));
        log.info("创建简单缓存管理器--->[simpleCacheManager]");
        return simpleCacheManager;
    }

    @Bean(name = "ehCacheCacheManager")
    public CacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        net.sf.ehcache.CacheManager manager = bean.getObject();
        cacheManager.setCacheManager(manager);
        log.info("创建ehCache缓存管理器--->[ehCacheCacheManager]");
        return cacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setCacheManagerName("ehCacheCacheManager");
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("config/cache/ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }
}
