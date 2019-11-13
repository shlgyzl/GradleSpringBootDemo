package com.application.repository;

import com.application.domain.Dam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface DamRepository extends JpaRepository<Dam, Long> {
    Stream<Dam> findAllByIdIsNotNull();
}
