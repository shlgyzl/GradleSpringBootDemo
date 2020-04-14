package com.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * 解决前端所传的token后端进行认证问题
 */
@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    /**
     * 也就是使用SpringSecurity的API解决此问题
     *
     * @param registry 注册中心
     */
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry registry) {
        registry
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/topic/**").authenticated()
                .simpDestMatchers("/user/topic/**").authenticated()
                .simpDestMatchers("/app/**").authenticated()
                .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        // 禁用同一个域不进行认证功能
        return true;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /*
         *  1. 配置一对一消息前缀， 客户端接收一对一消息需要配置的前缀 如“'/user/'+userid + '/message'”，
         *     是客户端订阅一对一消息的地址 stompClient.subscribe js方法调用的地址
         *  2. 使用@SendToUser发送私信的规则不是这个参数设定，在框架内部是用UserDestinationMessageHandler处理，
         *     而不是而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler
         *     or StompBrokerRelayMessageHandler，是在@SendToUser的URL前加“user+sessionId"组成
         */
        // 在发送消息给服务器之前需要带上前缀user
        registry.setUserDestinationPrefix("/user");

    }

}
