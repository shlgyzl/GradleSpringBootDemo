package com.application.web.resources.cache;

import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author yanghaiyong
 */
@Api(value = "EhCacheCache缓存管理接口", tags = {"EhCacheCache缓存管理接口"})
@RestController
@RequestMapping("api")
public class EhCacheCacheResources {

    @Resource
    private CacheManager cacheManager;


    @DynamicResponseParameters
    @ApiOperation(value = "查询接口", notes = "查询所有缓存")
    @Timed
    @GetMapping("/ehcaches")
    public ResponseEntity<CacheManager> findAll() {
        return ResponseEntity.ok(cacheManager);
    }

    @ApiOperation(value = "删除接口", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/ehcaches")
    public ResponseEntity<String> clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().body("清除ehCacheCacheManager成功");
    }
}
