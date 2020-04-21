package com.application.service;

import com.application.domain.jpa.Authority;
import com.application.repository.jpa.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public Authority saveOrUpdate(Authority authority) {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        return authorityRepository.save(authority);
    }
}
