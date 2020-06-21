package com.application.cache.service;

import com.application.cache.domain.EHCacheEntity;
import com.application.cache.domain.QEHCacheEntity;
import com.application.cache.repository.EHCacheEntityRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"简单缓存"}, cacheManager = {"simpleCacheManager"})
public class SimpleCacheService {
    private EHCacheEntityRepository ehCacheEntityRepository;

    private ConcurrentMapCacheManager concurrentMapCacheManager;

    public EHCacheEntity saveSimpleCache(EHCacheEntity eHCacheEntity) {
        return ehCacheEntityRepository.save(eHCacheEntity);
    }

    @CacheEvict(key = "#eHCacheEntity.id")
    public EHCacheEntity updateSimpleCache(EHCacheEntity eHCacheEntity) {
        return ehCacheEntityRepository.save(eHCacheEntity);
    }

    @CacheEvict(key = "#id")
    public void deleteById(Long id) {
        ehCacheEntityRepository.deleteById(id);
    }


    @Cacheable(key = "#id")
    public Optional<EHCacheEntity> findById(Long id) {
        return ehCacheEntityRepository.findById(id);
    }

    @Cacheable(key = "#eHCacheEntity")
    public Page<EHCacheEntity> findAll(EHCacheEntity eHCacheEntity, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(eHCacheEntity)) {
            QEHCacheEntity qehCacheEntity = QEHCacheEntity.eHCacheEntity;
            if (Objects.nonNull(eHCacheEntity.getId())) {
                builder.and(qehCacheEntity.id.eq(eHCacheEntity.getId()));
            }
        }
        return ehCacheEntityRepository.findAll(builder, pageable);
    }

    public List<Map<String, Object>> findCacheManager() {
        List<Map<String, Object>> result = new ArrayList<>(10);
        concurrentMapCacheManager.getCacheNames().forEach(name -> {
            Map<String, Object> context = new HashMap<>(2);
            context.put("cacheName", concurrentMapCacheManager.getCache(name));
            result.add(context);
        });
        return result;
    }


    public void clearCache() {
        concurrentMapCacheManager.getCacheNames()
                .forEach(name -> Objects.requireNonNull(concurrentMapCacheManager.getCache(name)).clear());
    }


}
