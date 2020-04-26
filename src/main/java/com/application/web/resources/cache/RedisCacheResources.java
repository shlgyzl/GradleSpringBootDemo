package com.application.web.resources.cache;


import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
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
@Api(value = "RedisCache缓存管理接口", tags = {"RedisCache缓存管理接口"})
@RestController
@RequestMapping("api")
public class RedisCacheResources {

    @Resource
    @Qualifier("redisCacheManager")
    private CacheManager cacheManager;


    @DynamicResponseParameters
    @ApiOperation(value = "查询接口", notes = "查询所有缓存")
    @Timed
    @GetMapping("/redis")
    public ResponseEntity<CacheManager> findAll() {
        return ResponseEntity.ok(cacheManager);
    }

    @ApiOperation(value = "删除接口", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/redis")
    public ResponseEntity<String> clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().body("清除RedisCacheManager成功");
    }
}
