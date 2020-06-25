package com.application.log.logback.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP 地址
 *
 * @author yanghaiyong
 * 2020/6/24-22:39
 */
public class IpLogConfiguration extends ClassicConverter {
    private static final Logger logger = LoggerFactory.getLogger(IpLogConfiguration.class);
    private static String localIp = null;

    @Override
    public String convert(ILoggingEvent event) {
        try {
            if (StringUtils.isBlank(localIp)) {
                localIp = InetAddress.getLocalHost().getHostAddress();
            }
            return localIp;
        } catch (UnknownHostException e) {
            logger.error("获取ip地址异常,exception={}", e.toString());
        }
        return null;
    }
}
