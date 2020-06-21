package com.application.jpa.web.rest;


import com.application.jpa.domain.Role;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.repository.RoleRepository;
import com.application.jpa.service.RoleService;
import com.application.jpa.web.rest.exception.BusinessErrorException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.types.Predicate;
import io.github.jhipster.web.util.ResponseUtil;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "Role", tags = {"Role角色管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class RoleResources {
    private final RoleRepository roleRepository;

    private final RoleService roleService;

    @ApiOperationSupport(ignoreParameters = {"users", "authorities"},order = 1)
    @ApiOperation(value = "保存接口", notes = "保存角色")
    @Timed
    @PostMapping("/role")
    public ResponseEntity<Role> save(@Valid @RequestBody Role role) throws URISyntaxException {
        Role savedRole = roleService.saveOrUpdate(role);
        return ResponseEntity.created(new URI("/api/role/" + savedRole.getId())).body(savedRole);
    }

    @ApiOperationSupport(ignoreParameters = {"users", "authorities"},order = 2)
    @ApiOperation(value = "更新接口", notes = "更新角色")
    @Timed
    @PutMapping("/role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws URISyntaxException {
        Role savedRole = roleService.saveOrUpdate(role);
        return ResponseEntity.created(new URI("/api/role/" + savedRole.getId())).body(savedRole);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "删除接口", notes = "删除角色")
    @Timed
    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "查询接口", notes = "查询角色(根据id)")
    @Timed
    @GetMapping("/role/{id}")
    public ResponseEntity<Role> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(roleRepository.findById(id));
    }

    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @PostMapping(value = "/roles")
    public ResponseEntity<Page<Role>> findAllRole(
            @RequestBody(required = false) Role role,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(roleService.findAll(role, pageable));
    }
}
