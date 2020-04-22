package com.application.resources.feign;

import com.application.feign.service.DamServiceFeign;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "DamFeign", tags = {"DamFeign远程大坝管理接口"})
@RestController
@RequestMapping("api")
public class DamFeignResources {
    @Resource
    private DamServiceFeign damServiceFeign;

    @ApiOperationSupport
    @ApiOperation(value = "查询接口", notes = "查询大坝")
    @GetMapping("/feign/dams")
    @Timed
    public List findAll() {
        return damServiceFeign.findSimpleDams();
    }
}
