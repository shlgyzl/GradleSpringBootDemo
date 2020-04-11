package com.application.repository.jpa;

import com.application.domain.jpa.Authority;
import com.application.domain.jpa.QAuthority;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import static com.application.resources.util.RepositoryUtil.common;

@SuppressWarnings("NullableProblems")
public interface AuthorityRepository extends BaseJpaRepository<Authority, Long>, QuerydslBinderCustomizer<QAuthority> {

    @EntityGraph(attributePaths = {"roles"})
    @Query("select A from Authority A where A.id = ?1 ")
    Authority findByOne(Long id);

    @Override
    default void customize(QuerydslBindings bindings, QAuthority root) {
        common(bindings, root.id, root.name);
    }
}
