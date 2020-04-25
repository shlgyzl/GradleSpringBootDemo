package com.application.web.resources;


import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Role;
import com.application.repository.jpa.RoleRepository;
import com.application.service.RoleService;
import com.application.web.resources.exception.BusinessErrorException;
import com.application.web.resources.util.JPAUtils;
import com.application.web.resources.util.ResponseUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperationSupport(ignoreParameters = {"users", "authorities"})
    @ApiOperation(value = "保存接口", notes = "保存角色")
    @Timed
    @PostMapping("/role")
    public ResponseEntity<Role> save(@Valid @RequestBody Role role) throws URISyntaxException {
        Role savedRole = roleService.saveOrUpdate(role);
        return ResponseEntity.created(new URI("/api/role/" + savedRole.getId())).body(savedRole);
    }

    @ApiOperationSupport(ignoreParameters = {"users", "authorities"})
    @ApiOperation(value = "更新接口", notes = "更新角色")
    @Timed
    @PutMapping("/role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) throws URISyntaxException {
        Role savedRole = roleService.saveOrUpdate(role);
        return ResponseEntity.created(new URI("/api/role/" + savedRole.getId())).body(savedRole);
    }

    @ApiParam(name = "id", value = "角色id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "删除接口", notes = "删除角色")
    @Timed
    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiParam(name = "id", value = "角色id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "查询接口", notes = "查询角色(根据id)")
    @Timed
    @GetMapping("/role/{id}")
    public ResponseEntity<Role> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(roleRepository.findById(id));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/roles")
    public ResponseEntity<Page<Role>> findAllRole(
            @QuerydslPredicate(root = Role.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok().body(roleRepository.findAll(predicate, pageable));
    }
}
