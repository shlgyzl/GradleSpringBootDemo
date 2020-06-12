package com.application.web.domain.abstracts;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.io.Serializable;

@Document(collection = "automatic")
@Data
public class Automatic implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field
    private String collectionName;

    @Field
    private Long seqId = 0L;
}
