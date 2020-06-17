package com.application.scheduling.config;

import io.github.jhipster.config.JHipsterProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 开启定时任务配置+异步任务线程池(该线程池只适用于定时任务)
 */
@Configuration
@EnableScheduling
public class SchedulingConfiguration implements SchedulingConfigurer {
    // 获取本系统的每个CPU的线程数
    private final static int num = Runtime.getRuntime().availableProcessors();
    private final JHipsterProperties jHipsterProperties;

    public SchedulingConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureTasks(@Nonnull ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executorTasks());
    }

    @Bean
    public Executor executorTasks() {
        int corePoolSize = jHipsterProperties.getAsync().getCorePoolSize();
        return Executors.newScheduledThreadPool(corePoolSize > num ? num : corePoolSize);
    }
}
