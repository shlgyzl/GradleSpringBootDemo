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
                .simpDestMatchers("/topic/tracker").authenticated()
                .simpDestMatchers("/topic/**").authenticated()
                .simpDestMatchers("/user/topic/**").authenticated()
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
        // 在发送消息给服务器之前需要带上前缀user
        registry.setUserDestinationPrefix("/user");
    }

}
