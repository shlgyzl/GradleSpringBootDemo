package com.application.config;

import com.application.constants.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class JacksonConfiguration {
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(@Qualifier("javaTimeModule") Module javaTimeModule, Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 序列化禁用日期转为时间戳
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 反序列化允许TimeZone被程序中覆盖
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 序列化全局对象属性序列化：凡是不是null的属性都会进行序列化
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        // NULL不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        // 指定时区，默认 UTC，而不是 jvm 默认时区
        objectMapper.setTimeZone(TimeZone.getDefault());
        // 全局日期类型返回风格
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        objectMapper.setDateFormat(simpleDateFormat);
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        return objectMapper;
    }
}
