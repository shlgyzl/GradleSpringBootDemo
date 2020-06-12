package com.application.web.repository.jpa;

import com.application.web.domain.jpa.PersistentToken;
import com.application.web.domain.jpa.QPersistentToken;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.common.util.RepositoryUtil.common;

public interface PersistentTokenRepository extends BaseJpaRepository<PersistentToken, String>,
        QuerydslBinderCustomizer<QPersistentToken> {
    @Override
    default void customize(QuerydslBindings bindings, QPersistentToken root) {
        common(bindings, root.user.id, root.user.login);
    }
}
