package com.application.cache.web.rest;

import com.application.cache.domain.EHCacheEntity;
import com.application.cache.service.SimpleCacheService;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.web.rest.exception.BusinessErrorException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.DynamicParameter;
import com.github.xiaoymin.knife4j.annotations.DynamicResponseParameters;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 处理简单缓存
 *
 * @author yanghaiyong
 */
@Api(value = "SimpleCache缓存管理接口", tags = {"SimpleCache缓存管理接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class SimpleCacheResources {
    private SimpleCacheService simpleCacheService;


    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "保存接口", notes = "保存SimpleCache简单缓存")
    @Timed
    @PostMapping("/simple-cache")
    public ResponseEntity<EHCacheEntity> save(@Valid @RequestBody EHCacheEntity eHCacheEntity)
            throws URISyntaxException {
        EHCacheEntity savedEHCacheEntity = simpleCacheService.saveSimpleCache(eHCacheEntity);
        return ResponseEntity.created(new URI("/api/simple-cache/" + savedEHCacheEntity.getId()))
                .body(savedEHCacheEntity);
    }

    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "更新接口", notes = "更新SimpleCache简单缓存")
    @Timed
    @PutMapping("/simple-cache")
    public ResponseEntity<EHCacheEntity> update(@Valid @RequestBody EHCacheEntity eHCacheEntity)
            throws URISyntaxException {
        if (Objects.isNull(eHCacheEntity.getId())) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        EHCacheEntity savedEHCacheEntity = simpleCacheService.updateSimpleCache(eHCacheEntity);
        return ResponseEntity.created(new URI("/api/simple-cache/" + savedEHCacheEntity.getId()))
                .body(savedEHCacheEntity);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "SimpleCache简单缓存id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除接口", notes = "删除SimpleCache简单缓存")
    @Timed
    @DeleteMapping("/simple-cache/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        simpleCacheService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "SimpleCache简单缓存id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询接口", notes = "查询SimpleCache简单缓存(根据id)")
    @Timed
    @GetMapping("/simple-cache/{id}")
    public ResponseEntity<EHCacheEntity> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseEntity.of(simpleCacheService.findById(id));
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @PostMapping(value = "/simple-caches")
    public ResponseEntity<Page<EHCacheEntity>> findAllEHCacheEntity(
            @RequestBody(required = false) EHCacheEntity eHCacheEntity,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(simpleCacheService.findAll(eHCacheEntity, pageable));
    }


    @ApiOperationSupport(responses = @DynamicResponseParameters(properties = {
            @DynamicParameter(value = "缓存名称", name = "cacheName", dataTypeClass = String.class)
    }), order = 6)
    @ApiOperation(value = "查询接口(查询所有缓存)", notes = "查询所有缓存")
    @Timed
    @GetMapping("/simple-caches/manager")
    public ResponseEntity<List<Map<String, Object>>> findCacheManager() {
        return ResponseEntity.ok(simpleCacheService.findCacheManager());
    }

    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除接口(清除所有缓存)", notes = "清除所有缓存")
    @Timed
    @DeleteMapping("/simple-caches/clear")
    public ResponseEntity<String> clearCache() {
        simpleCacheService.clearCache();
        return ResponseEntity.ok().body("清除simpleCacheManager成功");
    }
}
