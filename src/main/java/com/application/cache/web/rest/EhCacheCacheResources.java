package com.application.cache.web.rest;

import com.application.cache.web.rest.dto.CacheDTO;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * EhCacheCache缓存管理接口
 *
 * @author yanghaiyong
 */
@Api(value = "EhCacheCache缓存管理接口", tags = {"EhCacheCache缓存管理接口"})
@RestController
@RequestMapping("api")
public class EhCacheCacheResources {

    @Resource
    @Qualifier("ehCacheCacheManager")
    private CacheManager cacheManager;

    @DynamicResponseParameters
    @ApiOperation(value = "查询接口", notes = "查询所有缓存")
    @Timed
    @GetMapping("/ehcaches")
    @Cacheable
    public ResponseEntity<Set<CacheDTO>> findAll() {
        Set<CacheDTO> collect = cacheManager.getCacheNames().stream()
                .map(cacheName -> new CacheDTO(cacheName, cacheManager.getCache(cacheName)))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(collect);
    }

    @ApiOperation(value = "删除接口", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/ehcaches")
    public ResponseEntity<String> clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().body("清除ehCacheCacheManager成功");
    }
}
