package com.application.jpa.repository;

import com.application.jpa.domain.DefectType;
import com.application.jpa.domain.DefectTypeProperty;
import com.application.jpa.domain.QDefectTypeProperty;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Set;

import static com.application.jpa.common.util.RepositoryUtil.common;


public interface DefectTypePropertyRepository extends BaseJpaRepository<DefectTypeProperty, Long>,
        QuerydslBinderCustomizer<QDefectTypeProperty> {
    Integer deleteByDefectType(DefectType defectType);

    Set<DefectTypeProperty> findByDefectType(DefectType defectType);

    @Override
    default void customize(QuerydslBindings bindings, QDefectTypeProperty root) {
        common(bindings, root.id, root.name);
    }
}
