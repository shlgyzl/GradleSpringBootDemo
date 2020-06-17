package com.application.cache.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
@AllArgsConstructor
public class SpringRedisCacheConfiguration {
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
}
