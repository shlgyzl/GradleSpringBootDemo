package com.application.jpa.service;

import com.application.jpa.domain.QRole;
import com.application.jpa.domain.Role;
import com.application.jpa.repository.RoleRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public Role saveOrUpdate(Role role) {
        role.addAllAuthority(role.getAuthorities());
        return roleRepository.save(role);
    }

    public Page<Role> findAll(Role role, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(role)) {
            QRole qRole = QRole.role;
            if (StringUtils.hasText(role.getName())) {
                builder.and(qRole.name.containsIgnoreCase(role.getName()));
            }
        }
        return roleRepository.findAll(builder, pageable);
    }
}
