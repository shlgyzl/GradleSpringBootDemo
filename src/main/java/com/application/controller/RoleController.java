package com.application.controller;


import com.application.domain.jpa.Role;
import com.application.repository.jpa.RoleRepository;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "RoleController角色控制层", tags = {"Role角色信息接口"})
@RestController
@RequestMapping("api/admin")
@Slf4j
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * 角色列表(分页查询以及高级查询)
     *
     * @param pageable  分页查询
     * @param predicate 高级条件
     * @return Page<Role>
     */
    @ApiOperation(value = "角色列表", notes = "分页查询以及高级查询")
    @GetMapping("/role/index")
    public ResponseEntity<Page<Role>> index(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @QuerydslPredicate(root = Role.class) Predicate predicate) {
        log.debug("REST to request get Role list");
        return ResponseEntity.ok().body(roleRepository.findAll(predicate, pageable));
    }

    /**
     * 新增角色(只新增Role表和UserRole表)
     *
     * @param role 角色参数
     * @return Role
     * @throws URISyntaxException
     */
    @ApiOperation(value = "新增角色", notes = "只新增Role表和UserRole表")
    @PostMapping("/role")
    public ResponseEntity<Role> save(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST to request save a Role");
        @Valid Role save = roleRepository.save(role);
        return ResponseEntity.created(new URI("/api/admin/role/detail/" + save.getId())).body(save);
    }
}
