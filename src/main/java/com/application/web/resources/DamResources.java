package com.application.web.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Dam;
import com.application.repository.jpa.DamRepository;
import com.application.web.resources.exception.BusinessErrorException;
import com.application.web.resources.util.JPAUtils;
import com.application.web.resources.util.ResponseUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "Dam", tags = {"Dam大坝管理接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class DamResources {
    private final DamRepository damRepository;

    @ApiOperation(value = "保存接口", notes = "保存大坝")
    @Timed
    @PostMapping("/dam")
    public ResponseEntity<Dam> save(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @CachePut(value = {"ehCache"}, key = "#dam.name", cacheManager = "ehCacheCacheManager")
    @ApiOperation(value = "更新接口", notes = "更新大坝")
    @Timed
    @PutMapping("/dam")
    public ResponseEntity<Dam> update(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @CacheEvict(value = {"ehCache"}, key = "#id", cacheManager = "ehCacheCacheManager")
    @ApiParam(name = "大坝id", value = "id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "删除接口", notes = "删除大坝")
    @Timed
    @DeleteMapping("/dam/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // 已经保存成功,只是ResponseEntity没有序列化,会抛出IO异常,但是不影响
    @Cacheable(value = {"ehCache"}, key = "#id", cacheManager = "ehCacheCacheManager")
    @ApiParam(name = "大坝id", value = "id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "查询接口", notes = "查询大坝(根据id)")
    @Timed
    @GetMapping("/dam/{id}")
    public Dam find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        //return ResponseUtil.wrapOrNotFound(damRepository.findById(id));
        return damRepository.findById(id).get();
    }

    @Cacheable(value = {"ehCache"}, cacheManager = "ehCacheCacheManager")
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/dams")
    public ResponseEntity<Page<Dam>> findAllDam(
            @QuerydslPredicate(root = Dam.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        predicate = JPAUtils.mergePredicate(predicate, new BooleanBuilder());
        return ResponseEntity.ok().body(damRepository.findAll(predicate, pageable));
    }
}
