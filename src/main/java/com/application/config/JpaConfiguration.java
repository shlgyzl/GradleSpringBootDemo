package com.application.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.application.repository")
@EntityScan(basePackages = {
        "com.application.domain.jpa",
        "com.application.domain.mongodb",
        "com.application.domain.redis"})
@EnableJpaAuditing
public class JpaConfiguration {

}
