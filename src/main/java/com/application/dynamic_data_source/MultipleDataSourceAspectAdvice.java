package com.application.dynamic_data_source;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author yanghaiyong
 * 2020/7/4-9:32
 */
@Component
@Aspect
public class MultipleDataSourceAspectAdvice implements Ordered {
    private static final Logger logger = LoggerFactory.getLogger(MultipleDataSourceAspectAdvice.class);

    @Before("execution(* com..service..*.*(..))")
    public void changeDataSource(JoinPoint point) throws Throwable {
        DynamicDataSourceContextHolder.setDataSourceType("slave_one");
    }

//    @After("execution(* com..service..*.*(..))")
//    public void clearDataSource(JoinPoint point) {
//        //方法执行完毕之后，销毁当前数据源信息，进行垃圾回收。
//        DynamicDataSourceContextHolder.clearDataSourceType();
//    }

    @Override
    public int getOrder() {
        return 1;
    }
}
