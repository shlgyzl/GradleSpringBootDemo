package com.application.web.repository.redis;

import com.application.web.domain.redis.UserRedis;
import org.springframework.data.repository.CrudRepository;

public interface UserRedisRepository extends CrudRepository<UserRedis, Long> {
}
