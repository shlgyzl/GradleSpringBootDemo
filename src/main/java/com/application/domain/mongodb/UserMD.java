package com.application.domain.mongodb;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 2020年1月11日 现在出现的问题是：会自动生成重复的生成类，这个以后解决掉
 */
@Document(collection = "user")
@Data
public class UserMD implements Serializable {
    private static final long serialVersionUID = -694272422575878696L;

    @Id
    private Long id;

    //private User user;

    @Field
    @JsonIgnore
    private Long version = 0L;

}
