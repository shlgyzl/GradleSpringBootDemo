package com.application.jpa.web.rest;

import com.application.jpa.domain.Authority;
import com.application.jpa.domain.enumeration.BusinessErrorType;
import com.application.jpa.service.AuthorityService;
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
import java.util.Optional;

/**
 * @author yanghaiyong 2020年3月24日 23:19:28
 */

@Api(value = "Authority", tags = {"Authority权限管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class AuthorityResources {
    private final AuthorityService authorityService;

    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "保存接口", notes = "保存权限")
    @Timed
    @PostMapping("/authority")
    public ResponseEntity<Authority> save(@Valid @RequestBody Authority authority) throws URISyntaxException {
        Authority savedAuthority = authorityService.save(authority);
        return ResponseEntity.created(new URI("/api/authority/" + savedAuthority.getId())).body(savedAuthority);
    }


    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "更新接口", notes = "更新权限")
    @Timed
    @PutMapping("/authority")
    public ResponseEntity<Authority> update(@Valid @RequestBody Authority authority) throws URISyntaxException {
        Authority savedAuthority = authorityService.update(authority);
        return ResponseEntity.created(new URI("/api/authority/" + savedAuthority.getId())).body(savedAuthority);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "删除接口", notes = "删除权限")
    @Timed
    @DeleteMapping("/authority/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorityService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "权限id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "查询接口", notes = "查询权限(根据id)")
    @Timed
    @GetMapping("/authority/{id}")
    public ResponseEntity<Authority> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(authorityService.find(id)));
    }


    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/authorities")
    public ResponseEntity<Page<Authority>> findAll(
            @QuerydslPredicate(root = Authority.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok().body(authorityService.findAll(predicate, pageable));
    }
}
