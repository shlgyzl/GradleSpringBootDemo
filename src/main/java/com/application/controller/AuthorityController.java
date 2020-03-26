package com.application.controller;

import com.application.domain.jpa.Authority;
import com.application.domain.jpa.Role;
import com.application.repository.jpa.AuthorityRepository;
import com.application.repository.jpa.RoleRepository;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
    private final RoleRepository roleRepository;

    public AuthorityController(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
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

    /**
     * 更新权限(包括角色权限表)
     *
     * @param authority 权限参数
     * @return Role
     * @throws URISyntaxException
     */
    @ApiOperation(value = "更新权限", notes = "条件限制(包括角色权限表)")
    @PutMapping
    public ResponseEntity<Authority> update(@Valid @RequestBody Authority authority) throws URISyntaxException {
        log.debug("REST to request update a Authority");
        authority.getRoles().forEach(n -> n.setVersion(roleRepository.findVersion(n.getId())));
        @Valid Authority save = authorityRepository.save(authority);
        return ResponseEntity.created(new URI("api/authority/index" + save.getId())).body(save);
    }
}
