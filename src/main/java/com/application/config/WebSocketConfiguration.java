package com.application.config;

import com.application.constants.AuthoritiesConstants;
import com.application.websocket.WebSocketDecoratorFactory;
import com.application.websocket.WebsocketChannelInterceptor;
import io.github.jhipster.config.JHipsterProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

//@EnableWebSocketMessageBroker注解用于开启使用STOMP协议来传输基于代理（MessageBroker）的消息，这时候控制器（controller）
// 开始支持@MessageMapping,就像是使用@requestMapping一样。
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    public static final String IP_ADDRESS = "IP_ADDRESS";
    public static final String CONFERENCE_ID = "conferenceId";
    public static final String PLAT_FORM = "platform";
    public static final String TOPIC_IM = "/topic/conference/im/";
    public static final String TOPIC_ONLINE = "/topic/conference/onoffline/";

    private final JHipsterProperties jHipsterProperties;

    public WebSocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端接收服务端发送消息的前缀信息
        registry.enableSimpleBroker("/topic");
    }

    /**
     * 注册端点,启动连接作用
     *
     * @param registry 注册中心
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 获取跨域信息
        String[] cors = Optional.ofNullable(jHipsterProperties.getCors()
                .getAllowedOrigins()).map(n -> n.toArray(new String[0]))
                .orElse(new String[0]);
        registry.addEndpoint("/websocket/tracker")//设置端点名称用来接收客户端的连接
                .setAllowedOrigins(cors)//设置跨域信息
                .setHandshakeHandler(defaultHandshakeHandler())//设置默认握手处理器
                .addInterceptors(httpSessionHandshakeInterceptor())//设置握手拦截器
                .withSockJS();//用于模拟WebSocket技术,用于解决许多浏览器不支持WebSocket协议的问题
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
                    attributes.putAll(servletRequest.getServletRequest().getParameterMap());
                }
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

            }
        };
    }


    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                Principal principal = request.getPrincipal();
                if (principal == null) {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
                }
                return principal;
            }
        };
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(webSocketDecoratorFactory());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(rmeSessionChannelInterceptor());
    }

    @Bean
    public WebsocketChannelInterceptor rmeSessionChannelInterceptor() {
        return new WebsocketChannelInterceptor();
    }

    @Bean
    public WebSocketDecoratorFactory webSocketDecoratorFactory() {
        return new WebSocketDecoratorFactory();
    }
}
