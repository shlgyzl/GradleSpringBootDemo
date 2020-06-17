package com.application.security.config;

import com.application.security.config.jwt.JWTConfigurer;
import com.application.security.config.jwt.TokenProvider;
import com.application.security.config.provider.RemoteUserNamePasswordAuthenticationProvider;
import io.github.jhipster.config.JHipsterProperties;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    private final JHipsterProperties jHipsterProperties;

    private final RememberMeServices rememberMeServices;

    private final RemoteUserNamePasswordAuthenticationProvider remoteUserNamePasswordAuthenticationProvider;

    private final CorsFilter corsFilter;

    private final TokenProvider tokenProvider;

    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,
                                 JHipsterProperties jHipsterProperties, RememberMeServices rememberMeServices, RemoteUserNamePasswordAuthenticationProvider remoteUserNamePasswordAuthenticationProvider, CorsFilter corsFilter, TokenProvider tokenProvider) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.jHipsterProperties = jHipsterProperties;
        this.rememberMeServices = rememberMeServices;
        this.remoteUserNamePasswordAuthenticationProvider = remoteUserNamePasswordAuthenticationProvider;
        this.corsFilter = corsFilter;
        this.tokenProvider = tokenProvider;
    }

    @PostConstruct
    public void init() {
        try {
            authenticationManagerBuilder
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
            // 添加远程登录校验器
            authenticationManagerBuilder.authenticationProvider(remoteUserNamePasswordAuthenticationProvider);
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/h2-console/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/swagger-resources/configuration/ui")
                .antMatchers("/swagger-resources")
                .antMatchers("/swagger-resources/configuration/security")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/v2/api-docs")
                .antMatchers("/v2/api-docs-ext")
                .antMatchers("/test/**")
                .antMatchers("/**.{js,html}");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(corsFilter, CsrfFilter.class)
                .exceptionHandling();
        http.authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/authenticate/**").permitAll()
                .antMatchers("/api/**").authenticated();
        http.rememberMe()
                .rememberMeServices(rememberMeServices)
                .rememberMeParameter("remember-me")
                .key(jHipsterProperties.getSecurity().getRememberMe().getKey());
        http.headers().frameOptions().disable().and()
                .apply(new JWTConfigurer(tokenProvider));
    }
}
