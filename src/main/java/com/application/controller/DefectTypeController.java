package com.application.controller;

import com.application.domain.DefectType;
import com.application.repository.jpa.DefectTypePropertyRepository;
import com.application.repository.jpa.DefectTypeRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("detectType")
public class DefectTypeController {
    @Resource
    private DefectTypeRepository defectTypeRepository;

    @Resource
    private DefectTypePropertyRepository defectTypePropertyRepository;

    @ApiOperation(value = "保存接口", notes = "保存缺陷类型")
    @PostMapping("/save")
    public ResponseEntity<DefectType> save(@Valid @RequestBody DefectType defectType) {
        return ResponseEntity.ok(defectTypeRepository.save(defectType));
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
        Optional<DefectType> optional = defectTypeRepository.findById(id);
        Integer integer = defectTypePropertyRepository.deleteByDefectType(optional.get());
        return ResponseEntity.ok().body(optional.orElse(null));
    }

    @ApiOperation(value = "更新接口", notes = "更新缺陷类型")
    @PutMapping("/update")
    public ResponseEntity<DefectType> update(@RequestBody DefectType defectType) {
        DefectType save = defectTypeRepository.save(defectType);
        return ResponseEntity.ok().body(save);
    }
}
