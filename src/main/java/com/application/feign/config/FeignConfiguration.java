package com.application.feign.config;

import com.application.feign.service.DamServiceFeign;
import com.application.feign.service.UserServiceFeign;
import com.application.web.properties.ApplicationProperties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Import(FeignClientsConfiguration.class)
@Slf4j
public class FeignConfiguration implements InitializingBean {
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private FeignErrorDecoder feignErrorDecoder;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class,
                    new LocalDateTimeTypeAdapter()).create();
    private Map<Long, RequestTemplate> savedRequestTemplates = new ConcurrentHashMap<>();
    private String cookie;
    private long cookieVersion = 0;

    @Override
    public void afterPropertiesSet() {
        userServiceFeign = autoUserServiceFeign();// 所有属性初始化以后执行此方法
    }

    public UserServiceFeign autoUserServiceFeign() {// 自动登录
        Feign.Builder builder = Feign.builder().client(new Client.Default(null, null) {
            @Override
            public Response execute(Request request, Request.Options options) throws IOException {
                Response response = super.execute(request, options);
                cookie = response.headers().get("set-cookie").stream()
                        .filter(key -> key.contains("JSESSIONID"))
                        .findAny().orElse("");
                log.info("cookie refreshed,new cookie:{}", cookie);
                return response;
            }
        });
        return decorate(builder).target(UserServiceFeign.class, applicationProperties.getRemoteDam().getApiBaseUrl());
    }

    private Feign.Builder decorate(Feign.Builder builder) {
        return builder
                .decoder(new GsonDecoder(gson))
                .encoder(new GsonEncoder(gson))
                .logLevel(Logger.Level.BASIC);
    }

    private RequestInterceptor cookieInterceptor() {// 添加cookie拦截器
        return template -> {
            template.header("Cookie", cookie);
            savedRequestTemplates.put(Thread.currentThread().getId(), template);
        };
    }

    private Client.Default defaultClient() {
        return new Client.Default(null, null) {
            @Override
            public Response execute(Request request, Request.Options options) throws IOException {
                Response response = super.execute(request, options);
                if (response.status() == 401 || null == cookie) {
                    //保证cookie失效时只需登录一次
                    long currentVersion = cookieVersion;
                    synchronized (this) {
                        if (currentVersion == cookieVersion) {
                            log.info("cookie expire old cookie:{},current version:{}", cookie, currentVersion);
                            userServiceFeign.userLogin(applicationProperties.getRemoteDam().getUsername(), applicationProperties.getRemoteDam().getPassword());
                            cookieVersion++;
                        }
                        response = super.execute(savedRequestTemplates.remove(Thread.currentThread().getId()).request(), options);
                    }
                }
                return response;
            }
        };
    }

    private Feign.Builder commonBuilder() {
        return Feign.builder()
                // options方法指定连接超时时长及响应超时时长
                .options(new Request.Options(1000, 3500))
                // retryer方法指定重试策略
                .retryer(new Retryer.Default(5000, 5000, 3))
                .errorDecoder(feignErrorDecoder)
                .requestInterceptor(cookieInterceptor())
                .client(defaultClient());
    }

    private <T> T getCommonServiceFeign(Class<T> clazz, String url) {
        return decorate(commonBuilder()).target(clazz, url);// 立即执行
    }

    @Bean
    public UserServiceFeign userServiceFeign() {
        return decorate(Feign.builder()).target(UserServiceFeign.class, applicationProperties.getRemoteDam().getApiBaseUrl());
    }

    @Bean
    public DamServiceFeign damServiceFeign() {// 必须经过客户端登录保存Token设置Header,否则不通过
        return getCommonServiceFeign(DamServiceFeign.class, applicationProperties.getRemoteDam().getApiBaseUrl());
    }
}
