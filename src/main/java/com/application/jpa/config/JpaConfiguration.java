package com.application.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableJpaRepositories(basePackages = "com.application.jpa.repository")
@EntityScan(basePackages = {"com.application.**.domain"})
@EnableJpaAuditing
@EnableSpringDataWebSupport
public class JpaConfiguration {

}
