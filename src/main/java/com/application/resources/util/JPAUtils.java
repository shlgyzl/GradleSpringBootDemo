package com.application.resources.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanOperation;

public class JPAUtils {
    public static Predicate mergePredicate(Predicate oldOne, Predicate newOne) {
        return null != oldOne ? (((BooleanOperation) newOne).and(oldOne)) : (newOne);
    }
}
