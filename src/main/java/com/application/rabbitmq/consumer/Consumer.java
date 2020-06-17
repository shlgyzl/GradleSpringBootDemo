package com.application.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RabbitListener(queues = "executeTask")
public class Consumer {

    @RabbitHandler
    public void process(String message) {
        log.info("接收到消息：{}", message);
    }
}
