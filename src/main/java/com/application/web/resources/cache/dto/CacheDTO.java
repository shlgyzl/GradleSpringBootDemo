package com.application.web.resources.cache.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.cache.Cache;

import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CacheDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "name", value = "缓存实例名称", required = true, dataType = "String", example = "redis")
    private String name;

    @ApiModelProperty(name = "cache", value = "缓存实例内容", required = true, dataType = "Object", example = "Json内容")
    private Cache cache;
}
