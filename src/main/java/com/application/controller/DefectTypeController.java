package com.application.controller;

import com.application.controller.util.DomainUtil;
import com.application.controller.util.ResponseUtil;
import com.application.domain.jpa.DefectType;
import com.application.repository.jpa.DefectTypePropertyRepository;
import com.application.repository.jpa.DefectTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Api(value = "DefectTypeController缺陷类型控制层", tags = {"DetectType缺陷类型接口"})
@RestController
@RequestMapping("api/detectType")
public class DefectTypeController {
    private final DefectTypeRepository defectTypeRepository;
    private final DefectTypePropertyRepository defectTypePropertyRepository;

    public DefectTypeController(DefectTypeRepository defectTypeRepository,
                                DefectTypePropertyRepository defectTypePropertyRepository) {
        this.defectTypeRepository = defectTypeRepository;
        this.defectTypePropertyRepository = defectTypePropertyRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存缺陷类型")
    @PostMapping("/save")
    public ResponseEntity<DefectType> save(@Valid @RequestBody DefectType defectType) throws URISyntaxException {
        @Valid DefectType save = defectTypeRepository.save(defectType);
        return ResponseEntity.created(new URI("/api/detectType/find/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除缺陷类型")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        defectTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询缺陷类型")
    @GetMapping("/find/{id}")
    public ResponseEntity<DefectType> find(@PathVariable Long id) {
        Optional<DefectType> optional = defectTypeRepository.findByIdNoLazy(id);
        return ResponseUtil.wrapOrNotFound(optional);
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型")
    @PutMapping("/update")
    @Transactional
    public ResponseEntity<DefectType> update(@Valid @RequestBody DefectType defectType) {
        defectType.getDefectTypeProperties().forEach(n -> n.setVersion(defectTypePropertyRepository.findVersion(n.getId())));
        return ResponseEntity.ok().body(defectTypeRepository.save(defectType));
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型(过滤空字段)")
    @PutMapping("/update/filter")
    @Transactional
    public ResponseEntity<DefectType> updateFilterNull(@Valid @RequestBody DefectType defectType) {
        DefectType newObject = defectTypeRepository.findById(defectType.getId())
                .orElseThrow(() -> new NullPointerException(getClass().getSimpleName() + " id is null"));
        DomainUtil.copy(defectType, newObject);
        return ResponseEntity.ok().body(defectType);
    }
}
