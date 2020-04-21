package com.application.service;

import com.application.domain.jpa.Role;
import com.application.repository.jpa.RoleRepository;
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
