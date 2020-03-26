package com.application.repository.jpa;

import com.application.domain.jpa.QRole;
import com.application.domain.jpa.Role;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

import static com.application.controller.util.RepositoryUtil.common;

@SuppressWarnings("NullableProblems")
public interface RoleRepository extends BaseJpaRepository<Role, Long>, QuerydslBinderCustomizer<QRole> {
    @Override
    default void customize(QuerydslBindings bindings, QRole root) {
        common(bindings, root.id, root.name);
    }

    @EntityGraph(attributePaths = {"authorities"})
    @Override
    Page<Role> findAll(Predicate predicate, Pageable pageable);

    @EntityGraph(attributePaths = {"authorities"})
    @Override
    Optional<Role> findById(Long id);
}
