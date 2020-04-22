package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Authority;
import com.application.repository.jpa.AuthorityRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.application.service.AuthorityService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
public class AuthorityResources {
    private final AuthorityRepository authorityRepository;
    private final AuthorityService authorityService;

    public AuthorityResources(AuthorityRepository authorityRepository, AuthorityService authorityService) {
        this.authorityRepository = authorityRepository;
        this.authorityService = authorityService;
    }

    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "保存接口", notes = "保存权限")
    @Timed
    @PostMapping("/authority")
    public ResponseEntity<Authority> save(@Valid @RequestBody Authority authority) throws URISyntaxException {
        Authority savedAuthority = authorityService.saveOrUpdate(authority);
        return ResponseEntity.created(new URI("/api/authority/" + savedAuthority.getId())).body(savedAuthority);
    }

    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "更新接口", notes = "更新权限")
    @Timed
    @PutMapping("/authority")
    public ResponseEntity<Authority> update(@Valid @RequestBody Authority authority) throws URISyntaxException {
        Authority savedAuthority = authorityService.saveOrUpdate(authority);
        return ResponseEntity.created(new URI("/api/authority/" + savedAuthority.getId())).body(savedAuthority);
    }

    @ApiOperation(value = "删除接口", notes = "删除权限")
    @Timed
    @DeleteMapping("/authority/{id}")
    public ResponseEntity<Void> delete(@ApiParam(value = "主键id") @PathVariable Long id) {
        authorityRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询权限(根据id)")
    @Timed
    @GetMapping("/authority/{id}")
    public ResponseEntity<Authority> find(@ApiParam(value = "主键id") @PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(authorityRepository.findById(id));
    }


    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/authorities")
    public ResponseEntity<Page<Authority>> findAll(
            @QuerydslPredicate(root = Authority.class) Predicate predicate,
            @ApiIgnore @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        predicate = Optional.ofNullable(predicate).orElse(new BooleanBuilder());
        return ResponseEntity.ok().body(authorityRepository.findAll(predicate, pageable));
    }
}
