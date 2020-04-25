package com.application.repository.mongodb;

import com.application.domain.mongodb.QUserMongoDB;
import com.application.domain.mongodb.UserMongoDB;
import com.querydsl.core.types.dsl.SimpleExpression;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface UserMongoDBRepository extends BaseMongoDBRepository<UserMongoDB, Long>,
        QuerydslBinderCustomizer<QUserMongoDB> {
    @Override
    default void customize(QuerydslBindings bindings, QUserMongoDB root) {
        bindings.bind(root.id).first(SimpleExpression::eq);
    }
}
