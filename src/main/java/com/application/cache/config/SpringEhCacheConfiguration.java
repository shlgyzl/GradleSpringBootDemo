package com.application.cache.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@AllArgsConstructor
public class SpringEhCacheConfiguration {
//    private final JHipsterProperties jHipsterProperties;
//
//    @Bean
//    public JCacheManagerCustomizer cacheManagerCustomizer() {
//        // 增强Ehcache功能,没啥用
//        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();
//        javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
//                CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
//                        ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
//                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
//                        .build());
//
//        // 在此处添加需要cache的实体
//        return cm -> {
//            cm.createCache("eHcache", jcacheConfiguration);
//        };
//    }
//
//
//
//
//    @Bean(name = "ehCacheCacheManager")
//    public CacheManager ehCacheCacheManager(EhCacheManagerFactoryBean bean) {
//        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//        net.sf.ehcache.CacheManager manager = bean.getObject();
//        cacheManager.setCacheManager(manager);
//        log.info("创建ehCache缓存管理器--->[ehCacheCacheManager]");
//        return cacheManager;
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        cacheManagerFactoryBean.setCacheManagerName("ehCacheCacheManager");
//        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("config/cache/ehcache.xml"));
//        cacheManagerFactoryBean.setShared(true);
//        return cacheManagerFactoryBean;
//    }
}
