package com.application.jpa.service;

import com.application.jpa.domain.*;
import com.application.jpa.repository.UserRepository;
import com.application.jpa.service.dto.BookDTO;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

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
        JPAQuery<BookDTO> jpaQuery = new JPAQuery<BookDTO>(entityManager);
        JPAQuery<BookDTO> clone = jpaQuery.clone(entityManager);
        QUser qUser = QUser.user;
        QDefectType qDefectType = QDefectType.defectType;
        QAttachment qAttachment = QAttachment.attachment;
        QRole qRole = QRole.role;
        QAuthority qAuthority = QAuthority.authority;

        JPAQuery<BookDTO> query = jpaQuery
                .select(Projections.constructor(BookDTO.class, qUser.login, qUser.imageUrl, qAuthority.id.countDistinct()).skipNulls())
                .from(qUser)
                .join(qRole).on(qRole.id.eq(qUser.roles.any().id))
                .join(qAuthority).on(qAuthority.id.eq(qRole.authorities.any().id))
                .groupBy(qUser.login, qUser.imageUrl)
                .orderBy(qUser.login.asc(), qUser.imageUrl.asc())
                .restrict(new QueryModifiers(10L, 0L));
        List<BookDTO> fetch = query.distinct().fetch();
        int size = query
                .restrict(QueryModifiers.EMPTY)
                .fetch().size();

        PageImpl<BookDTO> bookDTOS = new PageImpl<>(fetch, pageable, size);
        System.out.println(bookDTOS);







        /*BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(user)) {
            QUser qUser = QUser.user;
            if (StringUtils.hasText(user.getLogin())) {
                builder.and(qUser.login.containsIgnoreCase(user.getLogin()));
            }
        }
        return userRepository.findAll(builder, pageable);*/
        return null;
    }
}
