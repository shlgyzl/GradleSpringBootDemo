package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.DefectType;
import com.application.repository.jpa.DefectTypeRepository;
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
    @PostMapping("/detectType")
    public ResponseEntity<DefectType> save(@Valid @RequestBody @ApiParam(name = "缺陷类型实体") DefectType defectType) throws URISyntaxException {
        @Valid DefectType save = defectTypeRepository.save(defectType);
        return ResponseEntity.created(new URI("/api/detectType/" + save.getId())).body(save);
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型")
    @PutMapping("/detectType")
    @Transactional
    public ResponseEntity<DefectType> update(@Valid @RequestBody @ApiParam(name = "缺陷类型实体") DefectType defectType) {
        @Valid DefectType save = defectTypeRepository.save(defectType);
        return ResponseEntity.ok().body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除缺陷类型")
    @DeleteMapping("/detectType/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询缺陷类型")
    @GetMapping("/detectType/{id}")
    @Transactional
    public ResponseEntity<DefectType> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARMETER_BIG_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(defectTypeRepository.findByIdNoLazy(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @GetMapping("/detectTypes-all")
    @Transactional
    public ResponseEntity<Iterable<DefectType>> findAllDefectType(@QuerydslPredicate(root = DefectType.class) Predicate predicate) {
        return ResponseEntity.ok().body(defectTypeRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @GetMapping(value = "/detectTypes-all", params = "page")
    @Transactional
    public ResponseEntity<Page<DefectType>> findPageAllDefectType(
            @QuerydslPredicate(root = DefectType.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(defectTypeRepository.findAll(predicate, pageable));
    }
}
