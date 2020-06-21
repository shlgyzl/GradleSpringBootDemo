package com.application.jpa.service;

import com.application.jpa.domain.QUser;
import com.application.jpa.domain.User;
import com.application.jpa.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User job(User user) {
        log.info("QuartzJobBean执行用户真正的业务,参数:[{}]", user);
        return user;
    }

    public User saveOrUpdate(User user) {
        user.addAllRole(user.getRoles());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Page<User> findAll(User user, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(user)) {
            QUser qUser = QUser.user;
            if (StringUtils.hasText(user.getLogin())) {
                builder.and(qUser.login.containsIgnoreCase(user.getLogin()));
            }
        }
        return userRepository.findAll(builder, pageable);
    }
}
