package com.application.controller;

import com.application.domain.jpa.Authority;
import com.application.domain.jpa.Role;
import com.application.repository.jpa.AuthorityRepository;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author yanghaiyong 2020年3月24日 23:19:28
 */

@Api(value = "AuthorityController权限控制层", tags = {"Authority权限接口"})
@RestController
@RequestMapping("api/authority")
@Slf4j
public class AuthorityController {
    private final AuthorityRepository authorityRepository;

    public AuthorityController(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @ApiOperation(value = "查询列表", notes = "条件限制(可分页和高级查询)")
    @GetMapping("index")
    public ResponseEntity<Collection<Authority>> index(
            @RequestParam(name = "page", defaultValue = "false", required = false) Boolean page,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @QuerydslPredicate(root = Role.class) Predicate predicate) {
        if (page) {// 分页
            return ResponseEntity.ok().body(Sets.newLinkedHashSet(authorityRepository.findAll(predicate, pageable)));
        }// 不分页
        return ResponseEntity.ok().body(Sets.newLinkedHashSet(authorityRepository.findAll(predicate)));
    }
}
