package com.application.config;

import cn.hutool.core.date.DateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.application.constants.DateFormatConstants.*;

/**
 * 非json传参日期转换器
 */
@Configuration
public class DateConverterConfiguration implements WebBindingInitializer {
    @Override
    public void initBinder(WebDataBinder binder) {
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalDate> localDateConverter() {
        return source -> LocalDate.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalTime> LocalTimeConverter() {
        return source -> LocalTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return source -> LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, Date> dateConverter() {
        return source -> DateUtil.parse(source.trim());
    }

    @Bean
    @ConditionalOnBean(name = "requestMappingHandlerAdapter")
    public Converter<String, ZonedDateTime> zonedDateTimeConverter() {
        return source -> ZonedDateTime.parse(source, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }
}
