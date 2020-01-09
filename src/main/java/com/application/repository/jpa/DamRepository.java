package com.application.repository.jpa;

import com.application.domain.jpa.Dam;

import java.util.stream.Stream;

public interface DamRepository extends BaseJpaRepository<Dam, Long> {
    Stream<Dam> findAllByIdIsNotNull();

}
