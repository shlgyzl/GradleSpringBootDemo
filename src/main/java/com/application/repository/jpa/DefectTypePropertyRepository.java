package com.application.repository.jpa;

import com.application.domain.jpa.DefectType;
import com.application.domain.jpa.DefectTypeProperty;
import com.application.domain.jpa.QDefectTypeProperty;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.Set;

import static com.application.web.resources.util.RepositoryUtil.common;


public interface DefectTypePropertyRepository extends BaseJpaRepository<DefectTypeProperty, Long>,
        QuerydslBinderCustomizer<QDefectTypeProperty> {
    Integer deleteByDefectType(DefectType defectType);

    Set<DefectTypeProperty> findByDefectType(DefectType defectType);

    @Override
    default void customize(QuerydslBindings bindings, QDefectTypeProperty root) {
        common(bindings, root.id, root.name);
    }
}
