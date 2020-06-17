package com.application.cache.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class RedisCacheEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "缓存id", required = true, dataType = "Long", example = "1")
    private Long id;
}
