package com.application.web.repository.jpa;

import com.application.web.domain.jpa.Authority;
import com.application.web.domain.jpa.QRole;
import com.application.web.domain.jpa.Role;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;

import static com.application.common.util.RepositoryUtil.common;

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

    //@EntityGraph(attributePaths = {"authorities"})
    @Query("select R from Role R where R.id in(?1)")
    LinkedHashSet<Role> findByAll(Collection<Long> ids);

    LinkedHashSet<Role> findRoleByAuthoritiesContains(Authority authority);
}
