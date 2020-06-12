package com.application.jpa;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.application.*.repository")
@EntityScan(basePackages = {"com.application.*.domain.*"})
@EnableJpaAuditing
public class JpaConfiguration {

}
