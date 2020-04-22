package com.application.web.resources.util;

import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

@SuppressWarnings("Convert2MethodRef")
public class RepositoryUtil {
    public static void common(QuerydslBindings bindings, NumberPath<Long> id, StringPath name) {
        bindings.bind(id).first((longNumberPath, right) -> longNumberPath.eq(right));
        bindings.bind(name).first((stringPath, str) -> stringPath.containsIgnoreCase(str));
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
