package com.application.repository.jpa;

import com.application.domain.Dam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface DamRepository extends BaseJpaRepository<Dam, Long> {
    Stream<Dam> findAllByIdIsNotNull();

}
