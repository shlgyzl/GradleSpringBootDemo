package com.application.config;

import com.application.constants.DateFormatConstants;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


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
     * @return JavaTimeModule
     */
    @Bean(name = "javaTimeModule")
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 序列化
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        // 反序列化
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_DATE_FORMAT).withZone(ZoneId.systemDefault())));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateFormatConstants.DEFAULT_TIME_FORMAT).withZone(ZoneId.systemDefault())));
        return javaTimeModule;
    }
}
