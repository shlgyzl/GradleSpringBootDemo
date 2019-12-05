package com.application.config;

import com.application.constants.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


@JsonComponent
@Slf4j
public class DateTimeConfiguration {
    /**
     * 全局配置日期类
     * 如果是接收前台参数,可以使用spring提供的@DateTimeFormat注解在接口参数或属性上指定格式
     * 如果是返回给前台,可以使用jackson注解@JsonFormat注解在属性上指定返回的格式
     * 任何时间类型都必须符合Java下的date/time格式标准
     * 使用时间jackson注解必须引入相应的Jackson注解包
     *
     * @return ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // NULL和""不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // PrettyPrinter 格式化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        // 指定时区，默认 UTC，而不是 jvm 默认时区
        objectMapper.setTimeZone(TimeZone.getDefault());
        // 日期类型处理(只能Date类型生效)
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        objectMapper.setDateFormat(simpleDateFormat);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        return objectMapper;
    }
}
