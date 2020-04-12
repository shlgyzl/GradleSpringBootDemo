package com.application.websocket.service;

import com.application.dto.IMDto;
import com.application.websocket.SocketManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import static com.application.config.WebSocketConfiguration.TOPIC_IM;

@Controller
@Slf4j
public class IMService {
    private final SimpMessageSendingOperations messagingTemplate;

    public IMService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 发送广播消息，所有订阅了此路径的用户都会收到此消息
     * 这里做个restful风格，其实无所谓，根据项目实际情况进行配置
     * restful风格的接口，在springMVC中，我们使用@PathVariable注解，
     * 在websocket stomp接口中，restful要使用@DestinationVariable
     *
     * @return
     */
    @MessageMapping("/topic/im")
    public void logReceive(@Payload IMDto imdto, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        Principal user = stompHeaderAccessor.getUser();
        log.info("公告controller 中获取用户登录令牌：{}", user.getName());
        log.info("公告拿到客户端传递分组参数:{}", imdto);

        log.info("receive conference[{}] message type: [{}] content:[{}]", imdto.getConferenceId(), imdto.getType(), imdto.getContent());
        sendToUser(imdto.getConferenceId(), TOPIC_IM + imdto.getConferenceId(), "测试发送消息给客户端");

    }

    public void sendToUser(Long conferenceId, String topic, Object body) {// 发送消息给客户端
        SocketManager.getParticipants(conferenceId)
                .forEach(login -> {
                    log.info("send to user {},topic:{},body:{}", login, topic, body.toString());
                    messagingTemplate.convertAndSendToUser(login, topic, body);
                });
    }
}
