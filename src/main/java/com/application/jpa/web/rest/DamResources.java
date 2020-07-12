package com.application.jpa.web.rest;

import com.application.jpa.domain.Dam;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.service.DamService;
import com.application.jpa.web.rest.exception.BusinessErrorException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
import java.util.Optional;

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "Dam", tags = {"Dam大坝管理接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class DamResources {
    private final DamService damService;

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "保存接口", notes = "保存大坝")
    @Timed
    @PostMapping("/dam")
    public ResponseEntity<Dam> save(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damService.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }


    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "更新接口", notes = "更新大坝")
    @Timed
    @PutMapping("/dam")
    public ResponseEntity<Dam> update(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damService.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "大坝id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除接口", notes = "删除大坝")
    @Timed
    @DeleteMapping("/dam/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damService.delete(id);
        return ResponseEntity.ok().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "大坝id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询接口", notes = "查询大坝(根据id)")
    @Timed
    @GetMapping("/dam/{id}")
    public ResponseEntity<Dam> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(damService.find(id)));
    }


    @ApiOperationSupport(includeParameters = {"page", "size", "sort"},order = 5)
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @PostMapping(value = "/dams")
    public ResponseEntity<Page<Dam>> findAllDam(
            @RequestBody(required = false) Dam dam,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(damService.findAll(dam, pageable));
    }
}
