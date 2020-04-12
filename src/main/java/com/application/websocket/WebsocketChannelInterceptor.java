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
        // 在消息发送之前调用，方法中可以对消息进行修改，如果此方法返回值为空，则不会发生实际的消息发送调用
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

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        /*
         * 1. 在消息接收完成之后调用，不管发生什么异常，可以用于消息发送后的资源清理
         * 2. 只有当preReceive 执行成功，并返回true才会调用此方法
         * 2. 适用于PollableChannels，轮询场景
         */
    }


    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        /*
         * 1. 在消息发送完成后调用，而不管消息发送是否产生异常，在次方法中，我们可以做一些资源释放清理的工作
         * 2. 此方法的触发必须是preSend方法执行成功，且返回值不为null,发生了实际的消息推送，才会触发
         */
    }


    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        /*
         * 1. 在检索到消息之后，返回调用方之前调用，可以进行信息修改，如果返回null,就不会进行下一步操作
         * 2. 适用于PollableChannels，轮询场景
         */
        return null;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // 在消息发送后立刻调用，boolean值参数表示该调用的返回值
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        /* 1. 在消息被实际检索之前调用，如果返回false,则不会对检索任何消息，只适用于(PollableChannels)，
         * 2. 在websocket的场景中用不到
         */
        return false;
    }
}

