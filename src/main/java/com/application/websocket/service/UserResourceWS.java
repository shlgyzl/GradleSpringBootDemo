package com.application.websocket.service;

import com.application.domain.jpa.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class UserResourceWS {

    @MessageMapping("/message")
    @SendTo({"/topic/message"})
    //@SendToUser(value = "/topic/message")
    public User save(@Payload User user) {
        return user;
    }
}
