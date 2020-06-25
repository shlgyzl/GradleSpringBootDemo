package com.application.log.logback.interceptor;

import com.application.log.logback.web.rest.util.LogPathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yanghaiyong
 * 2020/6/23-22:23
 */
@Component
public class LogInterceptor implements HandlerInterceptor {
    private final static String IP = "IP";
    private static final Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String xForwardedForHeader = httpServletRequest.getHeader("X-Forwarded-For");
        String remoteIp = httpServletRequest.getRemoteAddr();
        String logPathName = LogPathUtil.iPLogPath() + httpServletRequest.getSession().getId();
        LOGGER.info("request id:{}, client ip:{}, X-Forwarded-For:{}", logPathName, remoteIp, xForwardedForHeader);
        MDC.put(IP, logPathName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        String logPathName = MDC.get(IP);
        LOGGER.info("remove requestId ({}) from logger", logPathName);
        MDC.remove(IP);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
