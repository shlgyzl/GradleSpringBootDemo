package com.application.repository.jpa;

import com.application.domain.jpa.QRole;
import com.application.domain.jpa.Role;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

public interface RoleRepository extends BaseJpaRepository<Role, Long>, QuerydslBinderCustomizer<QRole> {
    /**
     * 重写查询方式
     *
     * @param bindings
     * @param root
     */
    @Override
    default void customize(QuerydslBindings bindings, QRole root) {
        bindings.bind(root.id).first(SimpleExpression::eq);
        bindings.bind(root.name).first(StringExpression::containsIgnoreCase);
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }

    @EntityGraph(attributePaths = {"authorities"})
    @Override
    Page<Role> findAll(Predicate predicate, Pageable pageable);
}
