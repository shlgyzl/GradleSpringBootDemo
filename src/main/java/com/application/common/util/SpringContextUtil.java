package com.application.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 直接通过Spring 上下文获取SpringBean,用于多线程环境
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象 这里重写了bean方法，起主要作用
     * example: getBean("userService")//注意： 类名首字母一定要小写,除非是开头连续几个大写字母的
     */
    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    /**
     * 根据class的getSimpleName获取bean
     * example: getBean("UserService")类名首字母可以大写
     */
    public static Object getBeanFromSimpleName(String simpleName) {
        return getBean(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1, simpleName.length()));
    }

}
