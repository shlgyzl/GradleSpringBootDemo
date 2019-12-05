package com.application.controller;

import com.application.domain.Dam;
import com.application.domain.User;
import com.application.repository.jpa.UserRepository;
import com.application.repository.jpa.dao.impl.UserDaoImpl;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(value = "UserController用户控制层", tags = {"User用户信息接口"})
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Resource
    private UserDaoImpl userDao;

    @Resource
    private UserRepository userRepository;

    @ApiOperation(value = "查询大坝下所属用户", notes = "条件限制")
    @GetMapping("/findByDamName")
    public ResponseEntity<List<User>> findByDamName(Dam dam) {
        return ResponseEntity.ok(userRepository.findByDamsName(dam.getName()));
    }

    @ApiOperation(value = "分页条件查询大坝", notes = "条件限制")
    @GetMapping("/findByPageForSearch")
    public ResponseEntity<Page<User>> findByPageForSearch(@QuerydslPredicate(root = User.class) Predicate predicate,
                                                          @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(userRepository.findAll(predicate, pageable));
    }

    @ApiOperation(value = "保存用户", notes = "条件限制")
    @PostMapping("/save")
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @ApiOperation(value = "更新用户", notes = "条件限制")
    @PostMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        int update = userRepository.updateForStatic(user.getLogin(), user.getId());
        if (update > 0) {
            log.debug("修改成功：{}", user);
        }
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @ApiOperation(value = "查询用户详情", notes = "条件限制")
    @GetMapping("/find/{id}")
    @Transactional
    public ResponseEntity<BeanMap> find(@PathVariable("id") User user) {
        // 如果使用了QueryDsl则自动查询user

        // 遇到懒加载属性,直接调用该属性的toString()方法,此方法将会发送一个连表查询,没有n+1的情况
        User load = userDao.load(user.getId());
        BeanMap beanMap = BeanMap.create(load);
        // 使用原生sql查询
        System.out.println(userRepository.findByIdForNative(user.getId()));
        // 原生分页加排序查询
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        System.out.println(userRepository.findByLogin("Admin", pageRequest).getContent());

        // 普通排序
        userRepository.findByAndSort("Admin", Sort.by("login").descending());
        // SQL函数(只能这样写,不能写其他任何形式)
        userRepository.findByAndSort("Admin", JpaSort.unsafe("LENGTH(login)"));
        // SQL函数
        // 返回普通查询列(List<Object[]>)
        System.out.println(userRepository.findByAsArrayAndSort("Admin", Sort.by("fn_len").descending()));


        // 简单等值查询
        // QBE查询需要：已经序列化的实体,属性不能懒加载;有Example和ExampleMatcher进行组合条件
        // 实体仓库需要继承QueryByExampleExecutor<T>
        // 仅仅支持starts/contains/ends/regex 模糊查询
        // 不支持such as login = ?0 or (login = ?1 and firstName = ?2)

        ExampleMatcher matching = ExampleMatcher.matching();
        ExampleMatcher matcher = matching.withIgnoreNullValues()// 忽略空值
                .withIgnoreCase("login")// 忽略属性login大小写
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);// 以该属性值结束
        System.out.println(userRepository.findOne(Example.of(user, matcher)));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("login", ExampleMatcher.GenericPropertyMatcher::endsWith)
                .withMatcher("password", ExampleMatcher.GenericPropertyMatcher::startsWith);
        System.out.println(userRepository.findOne(Example.of(user, exampleMatcher)));

        // 如果有多个属性对象则使用对象链进行匹配
        // 高级用法
        // ExampleMatcher.StringMatcher.EXACT 等值
        // ExampleMatcher.StringMatcher.DEFAULT 等值


        return ResponseEntity.ok().body(beanMap);
    }
}
