package com.application.jpa.service;

import com.application.jpa.domain.Role;
import com.application.jpa.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role saveOrUpdate(Role role) {
        role.addAllAuthority(role.getAuthorities());
        return roleRepository.save(role);
    }
}
