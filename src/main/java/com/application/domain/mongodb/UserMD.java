package com.application.domain.mongodb;

import com.application.domain.User;
import lombok.Data;
import org.mongodb.morphia.annotations.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "user")
@Data
public class UserMD {
    private static final long serialVersionUID = -694272422575878696L;

    @Id
    private Long id;

    private User user;
}
