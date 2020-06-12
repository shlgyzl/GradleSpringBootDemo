package com.application.security.service;

import com.application.web.domain.jpa.Authority;
import com.application.web.domain.jpa.QUser;
import com.application.web.domain.jpa.User;
import com.application.web.repository.jpa.UserRepository;
import com.application.security.domain.SecurityUser;
import com.google.common.collect.Sets;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
@Slf4j
public class DomainUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        QUser user = QUser.user;
        BooleanExpression expression = user.login.eq(login);
        // 返回的用户信息包含了角色,权限,用户名
        return Sets.newLinkedHashSet(userRepository.findAll(expression)).stream()
                .map(n -> createSpringSecurityUser(login, n))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found in the database"));
    }

    private SecurityUser createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new RuntimeException("User " + lowercaseLogin + " was not activated");
        }
        Set<Authority> collect = user.getRoles()
                .stream().flatMap(n -> n.getAuthorities().stream())
                .collect(Collectors.toSet());
        List<GrantedAuthority> grantedAuthorities = collect
                .stream().map(n -> new SimpleGrantedAuthority(n.getName())).distinct()
                .collect(Collectors.toList());
        return new SecurityUser(user.getLogin(), user.getPassword(), grantedAuthorities, user.getImageUrl());
    }
}
