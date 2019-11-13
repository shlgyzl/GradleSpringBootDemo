package com.application.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 用于放置HTTP请求头4年有效期
 */
public class CachingHttpHeadersFilter implements Filter {

    public static final int DEFAULT_DAYS_TO_LIVE = 1461; // 4 years
    public static final long DEFAULT_SECONDS_TO_LIVE = TimeUnit.DAYS.toMillis(DEFAULT_DAYS_TO_LIVE);

    // We consider the last modified date is the start up time of the server
    public final static long LAST_MODIFIED = System.currentTimeMillis();

    private long cacheTimeToLive = DEFAULT_SECONDS_TO_LIVE;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
        // Nothing to destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 设置最大有效期
        httpResponse.setHeader("Cache-Control", "max-age=" + cacheTimeToLive + ", public");
        httpResponse.setHeader("Pragma", "cache");

        // Setting Expires header, for proxy caching
        httpResponse.setDateHeader("Expires", cacheTimeToLive + System.currentTimeMillis());

        // Setting the Last-Modified header, for browser caching
        httpResponse.setDateHeader("Last-Modified", LAST_MODIFIED);

        chain.doFilter(request, response);
    }
}

