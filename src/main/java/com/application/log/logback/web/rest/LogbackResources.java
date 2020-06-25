package com.application.log.logback.web.rest;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;


@Api(value = "Logback", tags = {"Logback 日志功能接口"})
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LogbackResources {
    private final static String REQUEST_ID = "requestId";
    public static Logger logger = LoggerFactory.getLogger(LogbackResources.class);

    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "打印接口", notes = "打印Logback")
    @Timed
    @GetMapping("/logback/print")
    public ResponseEntity<Void> print() {
        logger.debug("hello");
        return ResponseEntity.ok().build();
    }
}
