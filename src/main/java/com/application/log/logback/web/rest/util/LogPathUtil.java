package com.application.log.logback.web.rest.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日志路径
 *
 * @author yanghaiyong
 * 2020/6/24-23:00
 */
public class LogPathUtil {
    private static String parentPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static String fullPath = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    private static String mediaPath = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

    public static String logPath() {
        return parentPath + "/" + fullPath + "-";
    }

    public static String iPLogPath() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        return hostAddress + "/" + parentPath + "/" + mediaPath + "-";
    }
}
