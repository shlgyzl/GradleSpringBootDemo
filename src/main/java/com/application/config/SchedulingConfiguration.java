package com.application.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 开启定时任务配置+异步任务线程池(该线程池只适用于定时任务)
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {
    // 获取本系统的每个CPU的线程数
    private final static int num = Runtime.getRuntime().availableProcessors();
    @Resource
    @Qualifier(value = "getAsyncExecutor")
    private Executor executor;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executorTasks());
    }

    public Executor executorTasks() {
        ThreadFactory factory = (ThreadPoolTaskExecutor) executor;
        return Executors.newScheduledThreadPool(num / 2, factory);
    }
}
