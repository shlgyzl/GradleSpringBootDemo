package com.application.rabbitmq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    public Queue helloQueue() {
        return new Queue("executeTask");// 创建一个任务消息队列
    }
}
