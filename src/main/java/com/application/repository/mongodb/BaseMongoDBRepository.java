package com.application.repository.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * mongodb基础仓库
 *
 * @param <T>
 * @param <ID>
 * @author yanghaiyong 2019年12月17日 20:23:12
 */
@NoRepositoryBean
public interface BaseMongoDBRepository<T, ID> extends MongoRepository<T, ID> {
}
