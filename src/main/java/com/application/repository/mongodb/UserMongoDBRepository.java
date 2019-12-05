package com.application.repository.mongodb;

import com.application.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserMongoDBRepository extends MongoRepository<User, Long> {

}
