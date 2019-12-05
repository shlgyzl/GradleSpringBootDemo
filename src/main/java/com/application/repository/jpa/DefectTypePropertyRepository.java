package com.application.repository.jpa;

import com.application.domain.DefectType;
import com.application.domain.DefectTypeProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DefectTypePropertyRepository extends BaseJpaRepository<DefectTypeProperty, Long> {
    Integer deleteByDefectType(DefectType defectType);

    Set<DefectTypeProperty> findByDefectType(DefectType defectType);
}
