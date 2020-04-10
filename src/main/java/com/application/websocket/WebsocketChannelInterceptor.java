package com.application.websocket;

import com.application.security.jwt.JWTConfigurer;
import com.application.security.jwt.TokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class WebsocketChannelInterceptor implements ChannelInterceptor {

    @Resource
    private TokenProvider tokenProvider;

    /**
     * 发送消息之前
     *
     * @param message 消息
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Optional.ofNullable(message.getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class))
                .ifPresent(map -> {
                    //从websocket传过来的native header取出jwt,转成authentication对象作为principle
                    MultiValueMap<String, String> multiValueMap = map;
                    Optional.ofNullable(multiValueMap.get(JWTConfigurer.AUTHORIZATION_HEADER)).ifPresent(list -> {
                        accessor.setUser(this.tokenProvider.getAuthentication(list.get(0)));
                    });
                });
        return message;
    }

}
