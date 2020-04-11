package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Authority;
import com.application.repository.jpa.AuthorityRepository;
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

/**
 * @author yanghaiyong 2020年3月24日 23:19:28
 */

@Api(value = "AuthorityResources权限控制层", tags = {"Authority权限接口"})
@RestController
@RequestMapping("api")
@Slf4j
public class AuthorityResources {
    private final AuthorityRepository authorityRepository;

    public AuthorityResources(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "权限名称", dataType = "String", paramType = "body", required = true, defaultValue = "超级管理员")
    })
    @ApiOperation(value = "保存接口", notes = "保存权限")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = Authority.class)
    })
    @Timed
    @PostMapping("/authority")
    @Transactional
    public ResponseEntity<Authority> save(@Valid @RequestBody @ApiParam(name = "权限实体") Authority authority) throws URISyntaxException {
        return getAuthorityResponseEntity(authority);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "Long", paramType = "body", required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "name", value = "权限名称", dataType = "String", paramType = "body", required = true, defaultValue = "超级管理员"),
            @ApiImplicitParam(name = "version", value = "权限版本", dataType = "Long", paramType = "body", required = true, defaultValue = "0")
    })
    @ApiOperation(value = "更新接口", notes = "更新权限")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = Authority.class)
    })
    @Timed
    @PutMapping("/authority")
    @Transactional
    public ResponseEntity<Authority> update(@Valid @RequestBody @ApiParam(name = "权限实体") Authority authority) throws URISyntaxException {
        return getAuthorityResponseEntity(authority);
    }

    private ResponseEntity<Authority> getAuthorityResponseEntity(@ApiParam(name = "权限实体") @RequestBody @Valid Authority authority) throws URISyntaxException {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        @Valid Authority save = authorityRepository.save(authority);
        return ResponseEntity.created(new URI("/api/authority/" + save.getId())).body(save);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "Long", paramType = "path", required = true, defaultValue = "1")
    })
    @ApiOperation(value = "删除接口", notes = "删除权限")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/authority/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorityRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "Long", paramType = "path", required = true, defaultValue = "1")
    })
    @ApiOperation(value = "查询接口", notes = "查询权限(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Authority.class)
    })
    @Timed
    @GetMapping("/authority/{id}")
    public ResponseEntity<Authority> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(authorityRepository.findById(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "Long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "name", value = "权限名称", dataType = "String", paramType = "query", defaultValue = "超级管理员"),
            @ApiImplicitParam(name = "version", value = "权限版本", dataType = "Long", paramType = "query", defaultValue = "0")
    })
    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Authority.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/authorities-all")
    public ResponseEntity<Iterable<Authority>> findAllAuthority(@QuerydslPredicate(root = Authority.class) Predicate predicate) {
        return ResponseEntity.ok().body(authorityRepository.findAll(predicate));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", dataType = "Long", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "name", value = "权限名称", dataType = "String", paramType = "query", defaultValue = "超级管理员"),
            @ApiImplicitParam(name = "version", value = "权限版本", dataType = "Long", paramType = "query", defaultValue = "0"),
    })
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Authority.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/authorities-all", params = "page")
    @Transactional
    public ResponseEntity<Page<Authority>> findPageAllAuthority(
            @QuerydslPredicate(root = Authority.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(authorityRepository.findAll(predicate, pageable));
    }
}
