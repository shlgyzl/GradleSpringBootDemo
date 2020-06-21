package com.application.jpa.web.rest;

import com.application.jpa.domain.DefectTypeProperty;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.service.DefectTypePropertyService;
import com.application.jpa.web.rest.exception.BusinessErrorException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
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

@Api(value = "DefectTypeProperty", tags = {"DefectTypeProperty缺陷类型属性管理接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class DefectTypePropertyResources {

    private final DefectTypePropertyService defectTypePropertyService;

    @ApiOperationSupport(ignoreParameters = {"defectType"}, order = 1)
    @ApiOperation(value = "保存接口", notes = "保存缺陷类型属性")
    @Timed
    @PostMapping("/defectTypeProperty")
    public ResponseEntity<DefectTypeProperty> save(@Valid @RequestBody DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        @Valid DefectTypeProperty save = defectTypePropertyService.save(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId())).body(save);
    }

    @ApiOperationSupport(ignoreParameters = {"defectType"}, order = 2)
    @ApiOperation(value = "更新接口", notes = "更新缺陷类型属性")
    @Timed
    @PutMapping("/defectTypeProperty")
    public ResponseEntity<DefectTypeProperty> update(@Valid @RequestBody DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        @Valid DefectTypeProperty save = defectTypePropertyService.update(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId())).body(save);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "缺陷属性类型id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除接口", notes = "删除缺陷类型属性")
    @Timed
    @DeleteMapping("/defectTypeProperty/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypePropertyService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "缺陷属性类型id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询接口", notes = "查询缺陷类型属性(根据id)")
    @Timed
    @GetMapping("/defectTypeProperty/{id}")
    public ResponseEntity<DefectTypeProperty> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(defectTypePropertyService.find(id)));
    }


    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @PostMapping(value = "/defectTypeProperties")
    public ResponseEntity<Page<DefectTypeProperty>> findAllDefectTypeProperty(
            @RequestBody(required = false) DefectTypeProperty defectTypeProperty,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(defectTypePropertyService.findAll(defectTypeProperty, pageable));
    }
}
