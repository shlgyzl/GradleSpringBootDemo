package com.application.config;

import com.application.filter.CachingHttpHeadersFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

import static com.application.helper.H2ServerStartHelper.initH2Console;

@Configuration
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory> {
    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);
    private final Environment env;

    public WebConfigurer(Environment env) {
        this.env = env;
    }

    @Override
    public void customize(WebServerFactory factory) {
        //System.out.println(env.getProperty("server.port"));
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initCachingHttpHeadersFilter(servletContext, dispatcherTypes);
        initH2Console(servletContext);
        log.info("Web application fully configured");
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> dispatcherTypes) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter());

        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/i18n/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/content/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/app/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }
}
