package com.application.domain.mongodb;

import com.application.domain.jpa.User;
import lombok.Data;
import org.mongodb.morphia.annotations.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "user")
@Data
public class UserMD {
    private static final long serialVersionUID = -694272422575878696L;

    @Id
    private Long id;

    @Field
    private String userId;
}
