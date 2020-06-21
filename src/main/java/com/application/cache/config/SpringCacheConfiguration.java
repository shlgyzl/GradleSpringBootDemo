package com.application.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;


/**
 * 此配置只适用于小型项目(存于JVM中),中大型项目则需要第三方插件
 * 此处说明：如果使用简单的JVM缓存不加缓存时间则使用后两者,如果使用redis或阿里巴巴的jetCache则开放前两者
 * 阿里巴巴的缓存application.yaml已经配置好
 * 独立的redis缓存需要开放第一个Bean即可
 */
@EnableCaching// 使用cglib代理和aspectj切入
//@EnableMethodCache(basePackages = "com.application")//激活Cached 注解 主要用于方法上的缓存
//@EnableCreateCacheAnnotation//激活CreateCache 注解 主要用于对某个自定义变量设为缓存
@Configuration
@Slf4j
public class SpringCacheConfiguration {

}
