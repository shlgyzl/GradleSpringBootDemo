package com.application.repository.jpa;

import com.application.domain.jpa.PersistentToken;
import com.application.domain.jpa.QPersistentToken;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.web.resources.util.RepositoryUtil.common;

public interface PersistentTokenRepository extends BaseJpaRepository<PersistentToken, String>,
        QuerydslBinderCustomizer<QPersistentToken> {
    @Override
    default void customize(QuerydslBindings bindings, QPersistentToken root) {
        common(bindings, root.user.id, root.user.login);
    }
}
