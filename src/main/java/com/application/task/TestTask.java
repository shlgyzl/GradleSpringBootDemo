package com.application.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestTask {

    @Async
    @Scheduled(cron = "0 0 23 * * ?")
    public void test01() {
        System.out.println(Thread.currentThread().getName()+"周期要把");
    }
}
