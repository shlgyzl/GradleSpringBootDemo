package com.application.web.resources.cache;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
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
import java.util.*;

/**
 * 处理简单缓存
 *
 * @author yanghaiyong
 */
@Api(value = "SimpleCache缓存管理接口", tags = {"SimpleCache缓存管理接口"})
@RestController
@RequestMapping("api")
public class SimpleCacheResources {
    @Resource
    private CacheManager cacheManager;


    @ApiOperationSupport(responses = @DynamicResponseParameters(properties = {
            @DynamicParameter(value = "缓存名称", name = "cacheName", dataTypeClass = String.class)
    }))
    @ApiOperation(value = "查询接口", notes = "查询所有缓存")
    @Timed
    @GetMapping("/simples")
    public ResponseEntity<List<Map<String, Object>>> findAll() {
        List<Map<String, Object>> result = new ArrayList<>(10);
        cacheManager.getCacheNames().forEach(name -> {
            Map<String, Object> context = new HashMap<>(2);
            context.put("cacheName", cacheManager.getCache(name));
            result.add(context);
        });
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "删除接口", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/simples")
    public ResponseEntity<String> clear() {
        cacheManager.getCacheNames().forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
        return ResponseEntity.ok().body("清除simpleCacheManager成功");
    }
}
