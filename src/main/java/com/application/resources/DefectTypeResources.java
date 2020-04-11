package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.DefectType;
import com.application.repository.jpa.DefectTypeRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "DefectTypeResources缺陷类型控制层", tags = {"DetectType缺陷类型接口"})
@RestController
@RequestMapping("api")
public class DefectTypeResources {
    private final DefectTypeRepository defectTypeRepository;

    public DefectTypeResources(DefectTypeRepository defectTypeRepository) {
        this.defectTypeRepository = defectTypeRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存缺陷类型")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = DefectType.class)
    })
    @Timed
    @PostMapping("/defectType")
    @Transactional
    public ResponseEntity<DefectType> save(@Valid @RequestBody @ApiParam(name = "缺陷类型实体") DefectType defectType) throws URISyntaxException {
        return getDefectTypeResponseEntity(defectType);
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = DefectType.class)
    })
    @Timed
    @PutMapping("/defectType")
    @Transactional
    public ResponseEntity<DefectType> update(@Valid @RequestBody @ApiParam(name = "缺陷类型实体") DefectType defectType) throws URISyntaxException {
        return getDefectTypeResponseEntity(defectType);
    }

    private ResponseEntity<DefectType> getDefectTypeResponseEntity(@ApiParam(name = "缺陷类型实体") @RequestBody @Valid DefectType defectType) throws URISyntaxException {
        defectType.addAllDefectTypeProperty(defectType.getDefectTypeProperties());
        @Valid DefectType save = defectTypeRepository.save(defectType);
        return ResponseEntity.created(new URI("/api/defectType/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除缺陷类型")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/defectType/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询缺陷类型(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectType.class)
    })
    @Timed
    @GetMapping("/defectType/{id}")
    public ResponseEntity<DefectType> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypeRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectType.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/defectTypes-all")
    public ResponseEntity<Iterable<DefectType>> findAllDefectType(@QuerydslPredicate(root = DefectType.class) Predicate predicate) {
        return ResponseEntity.ok().body(defectTypeRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectType.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/defectTypes-all", params = "page")
    @Transactional
    public ResponseEntity<Page<DefectType>> findPageAllDefectType(
            @QuerydslPredicate(root = DefectType.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(defectTypeRepository.findAll(predicate, pageable));
    }
}
