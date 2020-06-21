package com.application.websocket.config;

import com.application.security.config.jwt.JWTConfigurer;
import com.application.security.config.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Component
@Slf4j
public class WebsocketChannelInterceptor implements ChannelInterceptor {
    private final TokenProvider tokenProvider;

    public WebsocketChannelInterceptor(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @SuppressWarnings({"unchecked", "NullableProblems"})
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Optional.ofNullable(message.getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class))
                .ifPresent(map -> Optional.ofNullable(((MultiValueMap<String, String>) map).get(JWTConfigurer.AUTHORIZATION_HEADER)).ifPresent(list -> {
                    accessor.setUser(this.tokenProvider.getAuthentication(list.get(0)));
                }));
        return message;
    }
}
