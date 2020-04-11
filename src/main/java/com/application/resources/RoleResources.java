package com.application.resources;


import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Role;
import com.application.repository.jpa.RoleRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "RoleResources角色控制层", tags = {"Role角色信息接口"})
@RestController
@RequestMapping("api")
@Slf4j
public class RoleResources {

    private final RoleRepository roleRepository;

    public RoleResources(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存角色")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = Role.class)
    })
    @Timed
    @PostMapping("/role")
    @Transactional
    public ResponseEntity<Role> save(@Valid @RequestBody @ApiParam(name = "角色实体") Role role) throws URISyntaxException {
        return getRoleResponseEntity(role);
    }

    @ApiOperation(value = "更新接口", notes = "更新角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = Role.class)
    })
    @Timed
    @PutMapping("/role")
    @Transactional
    public ResponseEntity<Role> update(@Valid @RequestBody @ApiParam(name = "角色实体") Role role) throws URISyntaxException {
        return getRoleResponseEntity(role);
    }

    private ResponseEntity<Role> getRoleResponseEntity(@ApiParam(name = "角色实体") @RequestBody @Valid Role role) throws URISyntaxException {
        role.addAllAuthority(role.getAuthorities());
        @Valid Role save = roleRepository.save(role);
        return ResponseEntity.created(new URI("/api/role/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除角色")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/role/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询角色(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Role.class)
    })
    @Timed
    @GetMapping("/role/{id}")
    public ResponseEntity<Role> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(roleRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Role.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/roles-all")
    public ResponseEntity<Iterable<Role>> findAllRole(@QuerydslPredicate(root = Role.class) Predicate predicate) {
        return ResponseEntity.ok().body(roleRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Role.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/roles-all", params = "page")
    @Transactional
    public ResponseEntity<Page<Role>> findPageAllRole(
            @QuerydslPredicate(root = Role.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(roleRepository.findAll(predicate, pageable));
    }
}
