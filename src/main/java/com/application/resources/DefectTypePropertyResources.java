package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.DefectTypeProperty;
import com.application.repository.jpa.DefectTypePropertyRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "DefectTypePropertyResources缺陷类型属性控制层", tags = {"DefectTypeProperty缺陷类型属性接口"})
@RestController
@RequestMapping("api")
public class DefectTypePropertyResources {
    @Resource
    private DefectTypePropertyRepository defectTypePropertyRepository;

    @ApiOperation(value = "新增缺陷类型属性", notes = "POST请求")
    @PostMapping("/defectTypeProperty")
    @Transactional
    public ResponseEntity<DefectTypeProperty> save(@Valid @RequestBody @ApiParam(name = "缺陷类型属性实体") DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        @Valid DefectTypeProperty save = defectTypePropertyRepository.save(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId()))
                .body(save);
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型属性属性")
    @PutMapping("/defectTypeProperty")
    @Transactional
    public ResponseEntity<DefectTypeProperty> update(@Valid @RequestBody @ApiParam(name = "缺陷类型属性实体") DefectTypeProperty defectTypeProperty) {
        @Valid DefectTypeProperty save = defectTypePropertyRepository.save(defectTypeProperty);
        return ResponseEntity.ok().body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除缺陷类型属性")
    @DeleteMapping("/defectTypeProperty/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypePropertyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询缺陷类型属性")
    @GetMapping("/defectTypeProperty/{id}")
    @Transactional
    public ResponseEntity<DefectTypeProperty> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARMETER_BIG_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypePropertyRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @GetMapping("/defectTypeProperties-all")
    @Transactional
    public ResponseEntity<Iterable<DefectTypeProperty>> findAllDefectTypeProperty(@QuerydslPredicate(root = DefectTypeProperty.class) Predicate predicate) {
        return ResponseEntity.ok().body(defectTypePropertyRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @GetMapping(value = "/defectTypeProperties-all", params = "page")
    @Transactional
    public ResponseEntity<Page<DefectTypeProperty>> findPageAllDefectTypeProperty(
            @QuerydslPredicate(root = DefectTypeProperty.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(defectTypePropertyRepository.findAll(predicate, pageable));
    }
}
