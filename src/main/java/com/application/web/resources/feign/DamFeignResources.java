package com.application.web.resources.feign;

import com.application.feign.service.DamServiceFeign;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "DamFeign远程大坝管理接口", tags = {"DamFeign远程大坝管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class DamFeignResources {
    private final DamServiceFeign damServiceFeign;

    @ApiOperationSupport
    @ApiOperation(value = "查询接口", notes = "查询大坝")
    @GetMapping("/feign/dams")
    @Timed
    @Cacheable(value = "Dam")
    public List findAll() {
        return damServiceFeign.findSimpleDams();
    }
}
