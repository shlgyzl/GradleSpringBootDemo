package com.application.config;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import io.github.jhipster.config.JHipsterProperties;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Arrays;

/**
 * 此配置只适用于小型项目(存于JVM中),中大型项目则需要第三方插件
 * 此处说明：如果使用简单的JVM缓存不加缓存时间则使用后两者,如果使用redis或阿里巴巴的jetCache则开放前两者
 * 阿里巴巴的缓存application.yaml已经配置好
 * 独立的redis缓存需要开放第一个Bean即可
 */
@Configuration
@EnableCaching// 使用cglib代理和aspectj切入
@EnableMethodCache(basePackages = "com.application")//激活Cached 注解 主要用于方法上的缓存
@EnableCreateCacheAnnotation//激活CreateCache 注解 主要用于对某个自定义变量设为缓存
@Slf4j
public class SpringCacheConfiguration {
    private final JHipsterProperties jHipsterProperties;

    public SpringCacheConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @SuppressWarnings("unchecked")
    @Primary
    @Bean("redisCacheManager")
    public CacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory,
                                     Jackson2JsonRedisSerializer jackson2JsonRedisSerializer,
                                     RedisSerializer keySerializer) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration.entryTtl(Duration.ofMinutes(30L))// 缓存超时时间
                .disableCachingNullValues()// 空值不缓存
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer))// key序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(keySerializer));// value序列化
        return RedisCacheManager.builder(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

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
