package com.application.config;

import io.github.jhipster.async.ExceptionHandlingAsyncTaskExecutor;
import io.github.jhipster.config.JHipsterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置(实现接口则使用SpringBoot默认线程池,如果自定义则使用@Bean创建线程池)
 */
@Configuration
@EnableAsync
@Slf4j
public class AsyncExecutorConfiguration implements AsyncConfigurer {
    // 获取本系统的每个CPU的线程数
    private final static int num = Runtime.getRuntime().availableProcessors();
    private final JHipsterProperties jHipsterProperties;

    public AsyncExecutorConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    /**
     * 创建线程池,同时开启异步线程功能,当然也可以直接创建线程池Bean对象
     * 此线程池仅仅用于异步方法使用
     *
     * @return Executor
     */
    @Bean(name = "taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        // executor.setCorePoolSize(num/2);
        int corePoolSize = jHipsterProperties.getAsync().getCorePoolSize();
        executor.setCorePoolSize(corePoolSize > num ? num : corePoolSize);
        // 设置最大线程数
        // executor.setMaxPoolSize(num);
        executor.setMaxPoolSize(jHipsterProperties.getAsync().getMaxPoolSize());
        // 表示该次请求之后保留该链接60s,超过则断掉该链接否则复用该链接
        executor.setKeepAliveSeconds(60);
        // 设置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("gradle-spring-boot-");
        // 是否允许线程超时,默认false
        executor.setAllowCoreThreadTimeOut(false);
        // 设置队列的初始容量,任何正值使用LinkedBlockingQueue阻塞队列,其他值则启动SynchronousQueue异步队列
        // executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setQueueCapacity(jHipsterProperties.getAsync().getQueueCapacity());
        // 设置所有线程池中的线程为守护线程
        executor.setDaemon(true);
        // 处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程maximumPoolSize，
        // 如果三者都满了，使用handler处理被拒绝的任务
        // 当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止
        // 这样，线程池可以动态的调整池中的线程数。
        // 处理任务的最后一种道门,线程任务拒绝策略分4中,默认AbortPolicy是抛出RuntimeException会导致程序停止
        // CallerRunsPolicy调用者自己执行,不允许失败,影响性能;DiscardPolicy直接丢弃任务;DiscardOldestPolicy有选择丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        log.info("配置异步默认任务线程池Executor----------{}", executor);
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.warn("调用异步方法出现异常----------ex->{},method->{},params->",
                ex.getMessage(), method.getName(), params);
    }
}
