package com.application.filter.config;

import com.application.filter.filters.CachingHttpHeadersFilter;
import com.application.filter.filters.ShallowEtagHeaderAdvanceFilter;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;

@Configuration
public class FilterConfiguration {
    private final Logger log = LoggerFactory.getLogger(FilterConfiguration.class);
    private final JHipsterProperties jHipsterProperties;

    public FilterConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = jHipsterProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/management/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
        }
        return new CorsFilter(source);
    }

    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderAdvanceFilter();
    }

    @Bean
    public FilterRegistrationBean cachingHttpHeadersFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new CachingHttpHeadersFilter());
        // 拦截想要指定的资源和路径
        registration.addUrlPatterns("/i18n/*", "/app/*", "/static/*", "/api/**");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("cachingHttpHeadersFilter");
        registration.setAsyncSupported(true);
        return registration;
    }
}
