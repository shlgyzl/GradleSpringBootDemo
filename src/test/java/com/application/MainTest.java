package com.application;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ch.qos.logback.core.spi.FilterReply.ACCEPT;
import static ch.qos.logback.core.spi.FilterReply.DENY;

@SpringBootTest(classes = {Main.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MainTest {

    @Resource
    private LoggerBuilder loggerBuilder;

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

    @Test
    public void test02() {
        Logger logger = loggerBuilder.getLogger("data-log", Main.class);
        logger.info("你好呀");
    }

    public RollingFileAppender createRollingFileAppender(String name, Level level) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        RollingFileAppender appender = new RollingFileAppender();
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

    public ConsoleAppender createConsoleAppender() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ConsoleAppender appender = new ConsoleAppender();
        appender.setContext(context);
        appender.setName("file-console");
        appender.addFilter(createLevelFilter(Level.DEBUG));
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
        String fp = OptionHelper.substVars("${LOG_PATH}/" + name + "/backup/" + level.levelStr.toLowerCase() + "-%d{yyyy-MM-dd}.log.%i", context);
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


//    @Autowired
//    private Executor executor;
//
//    public static Logger logger = LoggerFactory.getLogger(Main.class);
//
//    @Test
//    public void test() {
//        ExecutorService taskExecutors = Executors.newCachedThreadPool();
//        // 运行10个task，启动了10个线程
//        for (int i = 0; i < 10; ++i) {
//            taskExecutors.submit(new MyTask(i + ""));
//        }
//
//        taskExecutors.shutdown();
//    }
}