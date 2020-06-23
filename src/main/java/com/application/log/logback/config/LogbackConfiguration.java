package com.application.log.logback.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ch.qos.logback.core.spi.FilterReply.ACCEPT;
import static ch.qos.logback.core.spi.FilterReply.DENY;

/**
 * Logback 日志分析
 *
 * @author yanghaiyong
 * 2020/6/23-19:00
 */
@Configuration
public class LogbackConfiguration {
    public RollingFileAppender<ILoggingEvent> createRollingFileAppender(String name, Level level) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<>();
        //这里设置级别过滤器
        appender.addFilter(createLevelFilter(level));
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(context);
        //appender的name属性
        appender.setName("file-" + level.levelStr.toLowerCase());
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss"));
        //设置文件名
        appender.setFile(OptionHelper.substVars("${LOG_PATH}/" + name + "/" + "data-log-" + level.levelStr.toLowerCase() + "-" + format + "-1.log", context));
        appender.setAppend(true);
        appender.setPrudent(false);
        //加入下面两个节点
        appender.setRollingPolicy(createSizeAndTimeBasedRollingPolicy(name, level, context, appender));
        appender.setEncoder(createEncoder(context));
        appender.start();
        return appender;
    }

    private SizeAndTimeBasedRollingPolicy createSizeAndTimeBasedRollingPolicy(String name, Level level,
                                                                              LoggerContext context,
                                                                              FileAppender appender) {
        //设置文件创建时间及大小的类
        SizeAndTimeBasedRollingPolicy policy = new SizeAndTimeBasedRollingPolicy();
        //文件名格式
        String fp = OptionHelper.substVars("${LOG_PATH}/" + name + "/backup/" + level.levelStr.toLowerCase() + "-%d{yyyy-MM-dd}.%i.log", context);
        //最大日志文件大小
        policy.setMaxFileSize(FileSize.valueOf("5MB"));
        //设置文件名模式
        policy.setFileNamePattern(fp);
        //设置最大历史记录为30条
        policy.setMaxHistory(30);
        //总大小限制
        policy.setTotalSizeCap(FileSize.valueOf("32GB"));
        //设置父节点是appender
        policy.setParent(appender);
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        policy.setContext(context);
        policy.start();
        return policy;
    }

    private PatternLayoutEncoder createEncoder(LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        //设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<contextName>设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        encoder.setContext(context);
        //设置格式
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} %msg%n");
        encoder.start();
        return encoder;
    }

    private LevelFilter createLevelFilter(Level level) {
        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setLevel(level);
        levelFilter.setOnMatch(ACCEPT);
        levelFilter.setOnMismatch(DENY);
        levelFilter.start();
        return levelFilter;
    }
}
