package com.application.jpa.service;

import com.application.jpa.domain.Authority;
import com.application.jpa.domain.QAuthority;
import com.application.jpa.repository.AuthorityRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public Authority save(Authority authority) {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        return authorityRepository.save(authority);
    }

    public Authority update(Authority authority) {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        return authorityRepository.save(authority);
    }

    public void delete(Long id) {
        authorityRepository.deleteById(id);
    }

    public Authority find(Long id) {
        return authorityRepository.findById(id).orElse(null);
    }

    public Page<Authority> findAll(Authority authority, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(authority)) {
            QAuthority qAuthority = QAuthority.authority;
            if (StringUtils.isNotBlank(authority.getName())) {
                builder.and(qAuthority.name.containsIgnoreCase(authority.getName()));
            }
        }
        return authorityRepository.findAll(builder, pageable);
    }
}
