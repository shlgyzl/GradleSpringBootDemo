package com.application.web.domain.redis;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@ToString
@EqualsAndHashCode
@Setter
@Getter
@RedisHash
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class UserRedis {
    @Id
    @ApiModelProperty(name = "id", value = "用户id", required = true, dataType = "Long", example = "1")
    private Long id;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "id", value = "用户名", required = true, dataType = "String", example = "落叶天涯")
    private String name;

    @NotNull
    @NonNull
    @ApiModelProperty(name = "version", value = "角色版本锁", required = true, dataType = "Long", example = "0")
    @Column(name = "version")
    @Version
    private Long version = 0L;
}
