package com.application.jpa.web.rest;

import com.application.jpa.domain.DefectType;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.repository.DefectTypeRepository;
import com.application.jpa.service.DefectTypeService;
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

@Api(value = "DefectType", tags = {"DetectType缺陷类型管理接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class DefectTypeResources {
    private final DefectTypeRepository defectTypeRepository;

    private final DefectTypeService defectTypeService;

    @ApiOperationSupport(ignoreParameters = {"defectTypeProperties"})
    @ApiOperation(value = "保存接口", notes = "保存缺陷类型")
    @Timed
    @PostMapping("/defectType")
    public ResponseEntity<DefectType> save(@Valid @RequestBody DefectType defectType) throws URISyntaxException {
        DefectType savedDefectType = defectTypeService.saveOrUpdate(defectType);
        return ResponseEntity.created(new URI("/api/defectType/" + savedDefectType.getId())).body(savedDefectType);
    }

    @ApiOperationSupport(ignoreParameters = {"defectTypeProperties"})
    @ApiOperation(value = "更新接口", notes = "更新缺陷类型")
    @Timed
    @PutMapping("/defectType")
    public ResponseEntity<DefectType> update(@Valid @RequestBody DefectType defectType) throws URISyntaxException {
        DefectType savedDefectType = defectTypeService.saveOrUpdate(defectType);
        return ResponseEntity.created(new URI("/api/defectType/" + savedDefectType.getId())).body(savedDefectType);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "缺陷类型id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "删除接口", notes = "删除缺陷类型")
    @Timed
    @DeleteMapping("/defectType/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "缺陷类型id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "查询接口", notes = "查询缺陷类型(根据id)")
    @Timed
    @GetMapping("/defectType/{id}")
    public ResponseEntity<DefectType> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypeRepository.findById(id));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/defectTypes")
    public ResponseEntity<Page<DefectType>> findAllDefectType(
            @QuerydslPredicate(root = DefectType.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok().body(defectTypeRepository.findAll(predicate, pageable));
    }
}
