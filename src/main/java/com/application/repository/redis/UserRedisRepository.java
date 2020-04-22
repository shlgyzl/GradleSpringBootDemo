package com.application.repository.redis;

import com.application.domain.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserRedis, Long> {
}
