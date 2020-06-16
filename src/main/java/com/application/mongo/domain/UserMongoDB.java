package com.application.mongo.domain;

import io.swagger.annotations.ApiModel;
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

@ApiModel(value = "UserMongoDB", description = "UserMongoDB 实体表")
@Document(collection = "user")
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
public class UserMongoDB implements Serializable {
    @Id
    @ApiModelProperty(name = "id", value = "用户id", required = true, dataType = "Long", example = "1")
    private Long id;

    @Field
    @ApiModelProperty(name = "version", value = "用户版本号", required = true, dataType = "Long", example = "0")
    private Long version = 0L;
}
