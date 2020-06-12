package com.application.web.repository.jpa;

import com.application.web.domain.jpa.DefectType;
import com.application.web.domain.jpa.QDefectType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Optional;

import static com.application.common.util.RepositoryUtil.common;

public interface DefectTypeRepository extends BaseJpaRepository<DefectType, Long>,
        QuerydslBinderCustomizer<QDefectType> {

    @Query("select DT from DefectType DT WHERE DT.id = ?1 ")
    @EntityGraph(attributePaths = {"defectTypeProperties"})
    Optional<DefectType> findByIdNoLazy(Long id);

    @Override
    default void customize(QuerydslBindings bindings, QDefectType root) {
        common(bindings, root.id, root.name);
    }
}
