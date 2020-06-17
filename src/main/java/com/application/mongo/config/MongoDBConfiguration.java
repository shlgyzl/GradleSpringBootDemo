package com.application.mongo.config;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

@Configuration
@EnableMongoRepositories(basePackages = "com.application.mongo.repository")
public class MongoDBConfiguration {
    @Resource
    private MongoDatabaseFactory mongoDatabaseFactory;

    @Bean
    public GridFSBucket getGridFSBuckets() {
        MongoDatabase db = mongoDatabaseFactory.getMongoDatabase();
        return GridFSBuckets.create(db);
    }
}
