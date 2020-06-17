package com.application.cache.web.rest;

import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 使用阿里巴巴封装的jetCache,优点：可以非常方便集群,可以本地,也可远程缓存
 *
 * @author yanghaiyong
 */
@Api(value = "JetCache缓存管理接口", tags = {"JetCache缓存管理接口"})
@RestController
@RequestMapping("api")
public class JCacheResources {
    private final CacheManager cacheManager;

    @Autowired
    public JCacheResources(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @DynamicResponseParameters
    @ApiOperation(value = "查询接口", notes = "查询所有缓存")
    @Timed
    @GetMapping("/jetcaches")
    public ResponseEntity<CacheManager> findAll() {
        return ResponseEntity.ok(cacheManager);
    }

    @ApiOperation(value = "删除接口", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/jetcaches")
    public ResponseEntity<String> clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().body("清除simpleCacheManager成功");
    }
}
