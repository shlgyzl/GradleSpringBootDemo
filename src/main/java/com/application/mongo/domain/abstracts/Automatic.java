package com.application.mongo.domain.abstracts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;

@Document(collection = "automatic")
@Getter
@Setter
public class Automatic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field
    private String collectionName;

    @Field
    private Long seqId = 0L;
}
