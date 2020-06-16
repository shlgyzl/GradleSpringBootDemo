package com.application.jpa.repository;

import com.application.jpa.domain.Dam;
import com.application.jpa.domain.QDam;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.stream.Stream;

import static com.application.jpa.common.util.RepositoryUtil.common;

public interface DamRepository extends BaseJpaRepository<Dam, Long>, QuerydslBinderCustomizer<QDam> {
    Stream<Dam> findAllByIdIsNotNull();

    @Override
    default void customize(QuerydslBindings bindings, QDam root) {
        common(bindings, root.id, root.name);
    }
}
