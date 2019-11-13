package com.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 开启审计功能
 */
@EnableJpaAuditing
@Configuration
public class JpaAuditingConfiguration {

}
