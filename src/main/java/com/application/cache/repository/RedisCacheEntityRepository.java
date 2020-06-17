package com.application.cache.repository;

import com.application.cache.domain.RedisCacheEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RedisCacheEntityRepository extends JpaRepository<RedisCacheEntity, Long> {


}
