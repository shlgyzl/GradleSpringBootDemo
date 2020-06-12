package com.application.cache.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * RedisCacheEntity
 *
 * @author yanghaiyong
 */
@Entity
@Table(name = "tbl_redis_cache_entity")
@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class RedisCacheEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "缓存id", required = true, dataType = "Long", example = "1")
    private Long id;

    @NotNull(message = "名称不能为空")
    @NonNull
    @Size(min = 1, max = 20, message = "名称不能为空或超过20个字符")
    @Column(length = 20, unique = true, nullable = false)
    @ApiModelProperty(name = "name", value = "名称", required = true, dataType = "String", example = "admin")
    private String name;


}
