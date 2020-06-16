package com.application.jpa.repository;

import com.application.jpa.domain.PersistentToken;
import com.application.jpa.domain.QPersistentToken;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.jpa.common.util.RepositoryUtil.common;

public interface PersistentTokenRepository extends BaseJpaRepository<PersistentToken, String>,
        QuerydslBinderCustomizer<QPersistentToken> {
    @Override
    default void customize(QuerydslBindings bindings, QPersistentToken root) {
        common(bindings, root.user.id, root.user.login);
    }
}
