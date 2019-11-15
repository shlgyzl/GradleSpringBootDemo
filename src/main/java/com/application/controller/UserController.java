package com.application.controller;

import com.application.domain.Dam;
import com.application.domain.QUser;
import com.application.domain.User;
import com.application.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
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
        return ResponseEntity.ok(userRepository.findByDamsName(dam.getName()));
    }

    @ApiOperation(value = "分页条件查询大坝", notes = "条件限制")
    @GetMapping("/findByPageForCondition")
    public ResponseEntity<List<User>> findByPageForCondition(Dam dam) {
        BooleanExpression expression = QUser.user.dams.any().name.eq(dam.getName());
        return ResponseEntity.ok(userRepository.findByDamsName(dam.getName()));
    }

    @ApiOperation(value = "分页条件查询大坝", notes = "条件限制")
    @PostMapping("/save")
    public ResponseEntity<Void> save(@RequestBody User user) {
        /*Dam dam = new Dam();
        dam.setName("洪峰");
        user.getDams().add(dam);
        user.setPassword("322");
        userRepository.save(user);*/
        user.setId(4L);
        userRepository.deleteById(user.getId());
        return ResponseEntity.ok().build();
    }
}
