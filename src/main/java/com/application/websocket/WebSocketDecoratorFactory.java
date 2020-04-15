package com.application.websocket;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;
import java.util.Optional;

import static com.application.config.WebSocketConfiguration.PLAT_FORM;

/**
 * 服务端和客户端在进行握手挥手时会被执行
 */
@Component
@Slf4j
public class WebSocketDecoratorFactory implements WebSocketHandlerDecoratorFactory {

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {

        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {// 连接确定之后
                // 在WebSocket协商成功并且WebSocket连接打开并准备好使用后调用。
                log.info("建立连接  sessionId = {}", session.getId());
                getLoginFromWebSocketSession(session).ifPresent(login -> {

                    String platform = getAttribute(session, PLAT_FORM);
                    log.info("user[{}] enter platform[{}]", login, platform);
                    SocketManager.add(login, session);
                });
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {// 连接关闭之后
                // 在任何一方关闭WebSocket连接之后或在发生传输错误之后调用。
                log.info("退出连接  sessionId = {}", session.getId());
                getLoginFromWebSocketSession(session).ifPresent(login -> {

                    String platform = getAttribute(session, PLAT_FORM);
                    log.info("user[{}] enter platform[{}]", login, platform);
                    SocketManager.remove(login);
                });
                super.afterConnectionClosed(session, closeStatus);
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
                // 在新的WebSocket消息到达时调用,也就是接受客户端信息并发发送
                /*
                获取客户端发送的消息,这里使用文件消息，也就是字符串进行接收
                消息可以通过字符串，或者字节流进行接收
                TextMessage String/byte[]接收均可以
                BinaryMessage byte[]接收
                */
                log.info("客户端发送消息" + webSocketMessage.getPayload().toString());
                TextMessage message = new TextMessage(webSocketMessage.getPayload().toString());
                /*
                这里直接是字符串，做群发，如果要指定发送，可以在前台平均ID，后台拆分后获取需要发送的人。
                也可以做一个单独的controller，前台把ID传递过来，调用方法发送，在登录的时候把所有好友的标识符传递到前台，
                然后通过标识符发送私信消息
                */
                //this.batchSendMessage(message);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                // 处理底层WebSocket消息传输中的错误,连接出现异常时触发
                if (session.isOpen()) {
                    session.close();
                }

                log.debug("链接出错，关闭链接......");
                //webSocketInfo.remove(session.getId());
                super.handleTransportError(session, exception);
            }

            @Override
            public boolean supportsPartialMessages() {
                // WebSocketHandler是否处理部分消息,API文档描述说是拆分消息，多次处理，没有实际使用过
                return super.supportsPartialMessages();
            }

            /**
             * 给所有在线用户发送消息（这里用的文本消息）
             * @param message
            public void batchSendMessage(TextMessage message) {

            Set<Map.Entry<String, WebSocketBeanSpring>> setInfo =
            webSocketInfo.entrySet();
            for (Map.Entry<String, WebSocketBeanSpring> entry : setInfo) {
            WebSocketBeanSpring bean = entry.getValue();
            try {
            bean.getSession().sendMessage(message);
            } catch (IOException e) {
            log.error(e.getMessage(), e);
            }
            }
            }
             */
            /**
             * 给指定用户发送消息
             * @param userId
             * @param message

            public void sendMessage(String userId, TextMessage message)
            {
            WebSocketBeanSpring bean = webSocketInfo.get(userId);
            try
            {
            bean.getSession().sendMessage(message);
            }
            catch (IOException e)
            {
            log.error(e.getMessage(), e);
            }
            }
             */
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

