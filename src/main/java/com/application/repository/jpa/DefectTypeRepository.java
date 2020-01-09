package com.application.repository.jpa;

import com.application.domain.jpa.DefectType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DefectTypeRepository extends BaseJpaRepository<DefectType, Long> {

    @Query("select DT from DefectType DT WHERE DT.id = ?1 ")
    @EntityGraph(attributePaths = {"defectTypeProperties"})
    Optional<DefectType> findByIdNoLazy(Long id);
}
