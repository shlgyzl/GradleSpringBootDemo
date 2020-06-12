package com.application.web.service;

import com.application.web.domain.jpa.Authority;
import com.application.web.repository.jpa.AuthorityRepository;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "simpleCache", cacheManager = "simpleCacheManager")
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public Authority save(Authority authority) {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        return authorityRepository.save(authority);
    }

    @CachePut(key = "#authority.name")
    public Authority update(Authority authority) {
        authority.addAllRole(authority.getRoles());// 只能新增和修改,不能删除关联表,待考虑
        return authorityRepository.save(authority);
    }

    @CacheEvict(key = "#id")
    public void delete(Long id) {
        authorityRepository.deleteById(id);
    }

    @Cacheable(key = "#id")
    public Authority find(Long id) {
        return authorityRepository.findById(id).orElse(null);
    }

    @Cacheable
    public Page<Authority> findAll(Predicate predicate, Pageable pageable) {
        return authorityRepository.findAll(predicate, pageable);
    }
}
