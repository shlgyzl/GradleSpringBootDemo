package com.application.resources.feign;

import com.application.feign.service.DamServiceFeign;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api")
public class DamFeignResources {
    @Resource
    private DamServiceFeign damServiceFeign;

    @ApiOperation(value = "查询接口", notes = "查询大坝")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询大坝", response = Map.class, responseContainer = "List")
    })
    @GetMapping("/feign/dams")
    @Timed
    public List findAll() {
        return damServiceFeign.findSimpleDams();
    }
}
