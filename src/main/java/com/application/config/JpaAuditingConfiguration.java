package com.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * 开启审计功能
 */
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        //使用jpa审计功能，保存数据时自动插入创建人id和更新人id
        return () -> {
            // 从session中获取当前用户
            return Optional.of("系统管理员");
        };
    }
}
