package com.application.websocket.service;

import com.application.jpa.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserServiceWS {

    @MessageMapping("/save/message")
    @SendTo({"/topic/save/message"})
    public User save(@Payload User user) {
        log.info("服务器收到用户保存消息:[{}]", user);
        return user;
    }

    @MessageMapping("/update/message")
    @SendToUser(value = "/topic/update/message")
    public User update(@Payload User user) {
        log.info("服务器收到用户更新消息:[{}]", user);
        return user;
    }
}
