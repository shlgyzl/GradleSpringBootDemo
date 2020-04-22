package com.application.domain.mongodb;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 2020年1月11日 现在出现的问题是：会自动生成重复的生成类，这个以后解决掉
 */
@Document(collection = "user")
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class UserMongoDB implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(name = "id", value = "用户id", required = true, dataType = "Long", example = "1")
    private Long id;

    @Field
    @ApiModelProperty(name = "version", value = "用户版本号", required = true, dataType = "Long", example = "0")
    private Long version = 0L;

}
