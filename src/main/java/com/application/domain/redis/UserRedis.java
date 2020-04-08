package com.application.domain.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

@Data
@ToString
@EqualsAndHashCode
@RedisHash
public class UserRedis {
    private Long id;
    private String name;
}
