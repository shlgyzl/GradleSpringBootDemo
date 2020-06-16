package com.application.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID> extends JpaRepository<T, ID>,
        QuerydslPredicateExecutor<T>, QueryByExampleExecutor<T>, JpaSpecificationExecutor<T> {
    @Query("select E.version from #{#entityName} E where E.id = ?1 ")
    Long findVersion(Long id);
}
