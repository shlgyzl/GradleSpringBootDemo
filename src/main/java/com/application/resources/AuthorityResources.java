package com.application.resources;

import com.application.domain.jpa.Authority;
import com.application.domain.jpa.Role;
import com.application.repository.jpa.AuthorityRepository;
import com.application.repository.jpa.RoleRepository;
import com.application.resources.util.DomainUtil;
import com.google.common.collect.Sets;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanghaiyong 2020年3月24日 23:19:28
 */

@Api(value = "AuthorityResources权限控制层", tags = {"Authority权限接口"})
@RestController
@RequestMapping("api/authority")
@Slf4j
public class AuthorityResources {
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;

    public AuthorityResources(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @ApiOperation(value = "查询列表", notes = "条件限制(可分页和高级查询)")
    @GetMapping("index")
    public ResponseEntity<Collection<Authority>> index(
            @RequestParam(name = "page", required = false) Boolean page,
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
    @ApiResponse(response = Authority.class, code = 201, message = "修改成功")
    @PutMapping
    @Transactional
    public ResponseEntity<Authority> update(@Valid @RequestBody @ApiParam(value = "权限信息") Authority authority) throws URISyntaxException {
        log.debug("REST to request update a Authority");
        Set<Role> roles = authority.getRoles().stream()
                .map(n -> DomainUtil.copy(n, roleRepository.findById(n.getId()).get()))
                .collect(Collectors.toSet());

        Authority copy = DomainUtil.copy(authority, authorityRepository.findByOne(authority.getId()));
        Authority save = authorityRepository.save(copy);

        if (ObjectUtils.isEmpty(roles)) {
            LinkedHashSet<Role> roleByAuthorities = roleRepository.findRoleByAuthoritiesContains(save);
            roleByAuthorities.forEach(n -> n.getAuthorities().remove(save));
        } else {
            List<Role> saveAll = roleRepository.saveAll(roles);
            saveAll.addAll(save.getRoles());
            saveAll.forEach(n -> n.getAuthorities().add(save));
        }

        return ResponseEntity.created(new URI("api/authority/index" + save.getId())).body(save);
    }
}
