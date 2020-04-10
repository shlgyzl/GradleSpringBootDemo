package com.application.websocket;

import com.application.websocket.service.IMService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;
import java.util.Optional;

import static com.application.config.WebSocketConfiguration.*;

/**
 * 服务端和客户端在进行握手挥手时会被执行
 */
@Component
@Slf4j
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Autowired
    private IMService imService;

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {

        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {// 连接确定之后
                log.info("建立连接  sessionId = {}", session.getId());
                getLoginFromWebSocketSession(session).ifPresent(login -> {

                    long conferenceId = Long.parseLong(getAttribute(session, CONFERENCE_ID));
                    String platform = getAttribute(session, PLAT_FORM);
                    log.info("user[{}] enter conference:[{}]", login, conferenceId);

                    SocketManager.add(login, session);
                    SocketManager.join(conferenceId, login);
                    imService.sendToUser(conferenceId, TOPIC_ONLINE + conferenceId, new UserOnOffLine(login, platform, "ONLINE"));

                });
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {// 连接关闭之后
                log.info("退出连接  sessionId = {}", session.getId());
                getLoginFromWebSocketSession(session).ifPresent(login -> {

                    long conferenceId = Long.parseLong(getAttribute(session, CONFERENCE_ID));
                    String platform = getAttribute(session, PLAT_FORM);
                    log.info("user[{}] exit conference:[{}]", login, conferenceId);

                    SocketManager.remove(login);
                    SocketManager.leave(conferenceId, login);
                    imService.sendToUser(conferenceId, TOPIC_ONLINE + conferenceId, new UserOnOffLine(login, platform, "OFFLINE"));

                });
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }

    private Optional<String> getLoginFromWebSocketSession(WebSocketSession session) {
        String login = null;
        Principal principal = session.getPrincipal();
        if (principal != null) {
            if (principal instanceof AnonymousAuthenticationToken) {
                login = principal.getName();
            }
            if (principal instanceof UsernamePasswordAuthenticationToken) {
                login = principal.getName();
            }
        }
        return Optional.ofNullable(login);
    }

    private String getAttribute(WebSocketSession session, String key) {
        Object o = session.getAttributes().get(key);
        if (o instanceof String[]) {
            return ((String[]) o)[0];
        }
        return o.toString();
    }

    @Getter
    @Setter
    @ToString
    private static class UserOnOffLine {

        private String login;
        private String platform;
        private String type;

        public UserOnOffLine(String login, String platform, String type) {
            this.login = login;
            this.platform = platform;
            this.type = type;
        }

    }

}

