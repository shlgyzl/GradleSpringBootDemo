package com.application;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {Main.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MainTest {

//    @Test
//    public void test() {
//        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//        ch.qos.logback.classic.Logger logbackLogger = context.getLogger("com.zaxxer.hikari.HikariDataSource");
//        logbackLogger.setAdditive(false);
//        logbackLogger.setLevel(Level.TRACE);
//        RollingFileAppender appender = new RollingFileAppender();
//        appender.setContext(context);
//        appender.setName("DataX_LOG");
//        appender.setFile(OptionHelper.substVars("${LOG_PATH}/${APPDIR}/data-log.log", context));
//        appender.setAppend(true);
//        //appender.setBufferSize(FileSize.valueOf("1kb"));
//        appender.setPrudent(false);// 默认不开放多个JVM写入一个文件
//        // 回滚策略
//        TimeBasedRollingPolicy policy = new TimeBasedRollingPolicy();
//        policy.setFileNamePattern(OptionHelper.substVars("${LOG_PATH}/${APPDIR}/data-log-%d{yyyy-MM-dd}.%i.log", context));
//        policy.setTotalSizeCap(FileSize.valueOf("32GB"));// 总大小
//        policy.setParent(appender);
//        policy.setContext(context);
//        policy.start();
//        //SizeAndTimeBasedFNATP basedFNATP = new SizeAndTimeBasedFNATP();// 触发回滚策略
//        //basedFNATP.setMaxFileSize(FileSize.valueOf("100MB"));
//        //basedFNATP.start();
//        //policy.setTimeBasedFileNamingAndTriggeringPolicy(basedFNATP);
//
//        // 日志显示格式
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setContext(context);
//        encoder.setPattern("%d{yyyy年MM月dd日 E HH:mm:ss} %-5level %logger Line:%-3L - %msg%n");
//        encoder.setCharset(Charset.forName("UTF-8"));
//        encoder.start();
//
//        appender.setRollingPolicy(policy);
//        appender.setEncoder(encoder);
//        appender.start();
//
//        logbackLogger.addAppender(appender);
//
//        logbackLogger.info("测试测试");
//
//    }
}