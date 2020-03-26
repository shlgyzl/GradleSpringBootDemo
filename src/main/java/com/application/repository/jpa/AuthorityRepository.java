package com.application.repository.jpa;

import com.application.domain.jpa.Authority;
import com.application.domain.jpa.QAuthority;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.controller.util.RepositoryUtil.common;

@SuppressWarnings("NullableProblems")
public interface AuthorityRepository extends BaseJpaRepository<Authority, Long>, QuerydslBinderCustomizer<QAuthority> {
    @Override
    default void customize(QuerydslBindings bindings, QAuthority root) {
        common(bindings, root.id, root.name);
    }
}
