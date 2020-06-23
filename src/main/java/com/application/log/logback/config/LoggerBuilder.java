package com.application.log.logback.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import cn.hutool.core.lang.UUID;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志工厂类
 *
 * @author yanghaiyong
 * 2020/6/23-19:02
 */
@Component
public class LoggerBuilder {
    private ConcurrentHashMap<String, Logger> container = new ConcurrentHashMap<>();

    @Autowired
    private LogbackConfiguration logbackConfiguration;


    public Logger getLogger(String name, Class<?> clazz) {
        Logger logger = container.get(name);
        if (logger != null) {
            return logger;
        }
        synchronized (LoggerBuilder.class) {
            logger = container.get(name);
            if (logger != null) {
                return logger;
            }
            logger = build(name, clazz);
            container.put(name, logger);
        }
        container.clear();
        return logger;
    }

    private Logger build(String name, Class<?> clazz) {
        // 新建日志追加器
        RollingFileAppender<ILoggingEvent> appender = logbackConfiguration.createRollingFileAppender(name, Level.DEBUG);
        // 获取Logback 日志配置类
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        // 获取已存在的日志Logger
        ch.qos.logback.classic.Logger logger = context.getLogger("com.zaxxer.hikari.HikariDataSource");
        //设置不向上级打印信息
        logger.setAdditive(false);
        logger.addAppender(appender);
        logger.iteratorForAppenders().forEachRemaining(iLoggingEventAppender -> System.out.println(iLoggingEventAppender.getName()));
        return logger;
    }
}
