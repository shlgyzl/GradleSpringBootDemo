package com.application.web.service;

import com.alicp.jetcache.anno.*;
import com.application.web.domain.jpa.DefectTypeProperty;
import com.application.web.repository.jpa.DefectTypePropertyRepository;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 使用JetCache
 */
@Service
@Slf4j
@AllArgsConstructor
@Transactional
@CacheConfig(cacheNames = {"jetCache"}, cacheManager = "redisCacheManager")
public class DefectTypePropertyService {
    private final DefectTypePropertyRepository defectTypePropertyRepository;

    public DefectTypeProperty save(DefectTypeProperty defectTypeProperty) {
        return defectTypePropertyRepository.save(defectTypeProperty);
    }

    @CacheUpdate(name = "jetCache", key = "#defectTypeProperty.id", value = "#defectTypeProperty")
    public DefectTypeProperty update(DefectTypeProperty defectTypeProperty) {
        return defectTypePropertyRepository.save(defectTypeProperty);
    }

    @CacheInvalidate(name = "jetCache", key = "#id")
    public void delete(Long id) {
        defectTypePropertyRepository.deleteById(id);
    }

    @Cached(name = "jetCache", key = "#id", cacheType = CacheType.BOTH, localLimit = 1, expire = 1, timeUnit = TimeUnit.HOURS)
    public DefectTypeProperty find(Long id) {
        return defectTypePropertyRepository.findById(id).orElse(null);
    }

    // 开启自动刷新缓存机制
    @Cached(name = "jetCache", cacheType = CacheType.BOTH, localLimit = 1, expire = 1, timeUnit = TimeUnit.HOURS)
    @CacheRefresh(refresh = 1800, stopRefreshAfterLastAccess = 3600, timeUnit = TimeUnit.SECONDS)
    public Page<DefectTypeProperty> findAll(Predicate predicate, Pageable pageable) {
        return defectTypePropertyRepository.findAll(predicate, pageable);
    }
}
