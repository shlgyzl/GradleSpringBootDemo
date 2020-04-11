package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.DefectTypeProperty;
import com.application.repository.jpa.DefectTypePropertyRepository;
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

@Api(value = "DefectTypePropertyResources缺陷类型属性控制层", tags = {"DefectTypeProperty缺陷类型属性接口"})
@RestController
@RequestMapping("api")
public class DefectTypePropertyResources {

    private final DefectTypePropertyRepository defectTypePropertyRepository;

    public DefectTypePropertyResources(DefectTypePropertyRepository defectTypePropertyRepository) {
        this.defectTypePropertyRepository = defectTypePropertyRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存缺陷属性类型")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = DefectTypeProperty.class)
    })
    @Timed
    @PostMapping("/defectTypeProperty")
    @Transactional
    public ResponseEntity<DefectTypeProperty> save(@Valid @RequestBody @ApiParam(name = "缺陷属性类型实体") DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        return getDefectTypePropertyResponseEntity(defectTypeProperty);
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷属性类型")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = DefectTypeProperty.class)
    })
    @Timed
    @PutMapping("/defectTypeProperty")
    @Transactional
    public ResponseEntity<DefectTypeProperty> update(@Valid @RequestBody @ApiParam(name = "缺陷属性类型实体") DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        return getDefectTypePropertyResponseEntity(defectTypeProperty);
    }

    private ResponseEntity<DefectTypeProperty> getDefectTypePropertyResponseEntity(@ApiParam(name = "缺陷属性类型实体") @RequestBody @Valid DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        defectTypeProperty.addDefectType(defectTypeProperty.getDefectType());
        @Valid DefectTypeProperty save = defectTypePropertyRepository.save(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除缺陷属性类型")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/defectTypeProperty/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypePropertyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询缺陷属性类型(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectTypeProperty.class)
    })
    @Timed
    @GetMapping("/defectTypeProperty/{id}")
    public ResponseEntity<DefectTypeProperty> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypePropertyRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectTypeProperty.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/defectTypeProperties-all")
    public ResponseEntity<Iterable<DefectTypeProperty>> findAllDefectTypeProperty(@QuerydslPredicate(root = DefectTypeProperty.class) Predicate predicate) {
        return ResponseEntity.ok().body(defectTypePropertyRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = DefectTypeProperty.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/defectTypeProperties-all", params = "page")
    @Transactional
    public ResponseEntity<Page<DefectTypeProperty>> findPageAllDefectTypeProperty(
            @QuerydslPredicate(root = DefectTypeProperty.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(defectTypePropertyRepository.findAll(predicate, pageable));
    }
}
