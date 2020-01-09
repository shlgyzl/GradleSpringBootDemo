package com.application.controller;

import com.application.domain.jpa.DefectTypeProperty;
import com.application.repository.jpa.DefectTypePropertyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Api(value = "DefectTypePropertyController缺陷类型控制层", tags = {"DefectTypeProperty缺陷类型接口"})
@RestController
@RequestMapping("api/defectTypeProperty")
public class DefectTypePropertyController {
    @Resource
    private DefectTypePropertyRepository defectTypePropertyRepository;

    @ApiOperation(value = "新增缺陷类型属性", notes = "POST请求")
    @PostMapping("/save")
    public ResponseEntity<DefectTypeProperty> save(@Valid @RequestBody DefectTypeProperty defectTypeProperty) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(defectTypePropertyRepository.save(defectTypeProperty));
    }
}
