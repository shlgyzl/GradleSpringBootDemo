package com.application.repository.jpa;

import com.application.domain.jpa.DefectType;
import com.application.domain.jpa.DefectTypeProperty;

import java.util.Set;

public interface DefectTypePropertyRepository extends BaseJpaRepository<DefectTypeProperty, Long> {
    Integer deleteByDefectType(DefectType defectType);

    Set<DefectTypeProperty> findByDefectType(DefectType defectType);
}
