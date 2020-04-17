package com.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.messaging.access.intercept.ChannelSecurityInterceptor;
import org.springframework.security.messaging.access.intercept.MessageSecurityMetadataSource;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;

import java.util.List;

/**
 * 解决前端所传的token后端进行认证问题
 */
@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                //任何没有目的地的消息（即消息类型为MESSAGE或SUBSCRIBE以外的任何消息）将要求用户进行身份验证
                .nullDestMatcher().authenticated()
                //任何人都可以订阅/ user / queue / error
                .simpSubscribeDestMatchers("/user/queue/errors").authenticated()
                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).authenticated()
                //拒绝任何其他消息。这是一个好主意，以确保您不会错过任何消息。
                .anyMessage().denyAll();
    }



    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
