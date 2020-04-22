package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.DefectTypeProperty;
import com.application.repository.jpa.DefectTypePropertyRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Api(value = "DefectTypeProperty", tags = {"DefectTypeProperty缺陷类型属性管理接口"})
@RestController
@RequestMapping("api")
public class DefectTypePropertyResources {

    private final DefectTypePropertyRepository defectTypePropertyRepository;

    public DefectTypePropertyResources(DefectTypePropertyRepository defectTypePropertyRepository) {
        this.defectTypePropertyRepository = defectTypePropertyRepository;
    }

    @ApiOperationSupport(ignoreParameters = {"defectType"})
    @ApiOperation(value = "保存接口", notes = "保存权限类型属性")
    @Timed
    @PostMapping("/defectTypeProperty")
    public ResponseEntity<DefectTypeProperty> save(@Valid @RequestBody DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        @Valid DefectTypeProperty save = defectTypePropertyRepository.save(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId())).body(save);
    }

    @ApiOperationSupport(ignoreParameters = {"defectType"})
    @ApiOperation(value = "更新接口", notes = "更新权限类型属性")
    @Timed
    @PutMapping("/defectTypeProperty")
    public ResponseEntity<DefectTypeProperty> update(@Valid @RequestBody DefectTypeProperty defectTypeProperty) throws URISyntaxException {
        @Valid DefectTypeProperty save = defectTypePropertyRepository.save(defectTypeProperty);
        return ResponseEntity.created(new URI("/api/defectTypeProperty/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除权限类型属性")
    @Timed
    @DeleteMapping("/defectTypeProperty/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypePropertyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询权限类型属性(根据id)")
    @Timed
    @GetMapping("/defectTypeProperty/{id}")
    public ResponseEntity<DefectTypeProperty> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypePropertyRepository.findById(id));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/defectTypeProperties")
    public ResponseEntity<Page<DefectTypeProperty>> findAllDefectTypeProperty(
            @QuerydslPredicate(root = DefectTypeProperty.class) Predicate predicate,
            @ApiIgnore @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        predicate = Optional.ofNullable(predicate).orElse(new BooleanBuilder());
        return ResponseEntity.ok().body(defectTypePropertyRepository.findAll(predicate, pageable));
    }
}
