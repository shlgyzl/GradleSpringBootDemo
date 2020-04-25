package com.application.repository.jpa;

import com.application.domain.jpa.Dam;
import com.application.domain.jpa.QDam;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.stream.Stream;

import static com.application.web.resources.util.RepositoryUtil.common;

public interface DamRepository extends BaseJpaRepository<Dam, Long>, QuerydslBinderCustomizer<QDam> {
    Stream<Dam> findAllByIdIsNotNull();

    @Override
    default void customize(QuerydslBindings bindings, QDam root) {
        common(bindings, root.id, root.name);
    }
}
