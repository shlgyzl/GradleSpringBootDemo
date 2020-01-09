package com.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class MainTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void exampleTest() throws Exception {
        this.mvc.perform(get("/hello")).andExpect(status().isOk())
                .andExpect(content().string("Welcome to springboot2 world ~"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    @Test
    public void send() {
        String msg = "测试RabbitMQ";
        //发送消息
        rabbitmqTemplate.convertAndSend("executeTask", msg);
        log.info("消息：{},已发送", msg);

        long a = 1;
        System.out.println(a);
    }
}
