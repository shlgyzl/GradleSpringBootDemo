package com.application.scheduling.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestTask {

    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    public void task() {
        log.info("触发定时任务...开启线程：" + Thread.currentThread().getName() + "->执行任务");
    }
}
