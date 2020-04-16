package com.application.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class QuartzConfiguration {

    private final Scheduler scheduler;

    public QuartzConfiguration(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    private void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("scheduler init fail,cause:{}", e.getMessage());
        }
    }
}
