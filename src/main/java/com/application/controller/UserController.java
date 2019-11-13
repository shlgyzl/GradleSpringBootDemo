package com.application.controller;

import com.application.domain.Dam;
import com.application.domain.User;
import com.application.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(value = "UserController用户控制层", tags = {"User用户信息接口"})
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserRepository userRepository;

    @ApiOperation(value = "查询大坝下所属用户", notes = "条件限制")
    @GetMapping("/findByDamName")
    public ResponseEntity<List<User>> findByDamName(Dam dam) {

        List<User> byDamsNamesss = userRepository.findByDamsNamesss(dam.getName());
        return ResponseEntity.ok(byDamsNamesss);
    }
}
