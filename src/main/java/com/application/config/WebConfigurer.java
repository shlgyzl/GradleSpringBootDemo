package com.application.config;

import com.application.filter.CachingHttpHeadersFilter;
import com.application.service.AttachmentService;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfigurer implements ServletContextInitializer, WebServerFactoryCustomizer<WebServerFactory>, WebMvcConfigurer {
    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);
    private final Environment env;
    private final JHipsterProperties jHipsterProperties;
    private final AttachmentService attachmentService;

    public WebConfigurer(Environment env, JHipsterProperties jHipsterProperties, AttachmentService attachmentService) {
        this.env = env;
        this.jHipsterProperties = jHipsterProperties;
        this.attachmentService = attachmentService;
    }

    @Override
    public void customize(WebServerFactory factory) {
        setMimeMappings(factory);// 处理其他请求方式的中文乱码,比如websocket
    }

    private void setMimeMappings(WebServerFactory server) {
        if (server instanceof ConfigurableServletWebServerFactory) {
            MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
            // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
            mappings.add("html", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
            mappings.add("json", MediaType.TEXT_HTML_VALUE + ";charset=" + StandardCharsets.UTF_8.name().toLowerCase());
            ConfigurableServletWebServerFactory servletWebServer = (ConfigurableServletWebServerFactory) server;
            servletWebServer.setMimeMappings(mappings);
        }
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        dispatcherTypes.remove(DispatcherType.ERROR);
        //initCachingHttpHeadersFilter(servletContext, dispatcherTypes);
        // 使用WEB则需配置访问路径
//        initH2Console(servletContext);
        // 使用TCP则需要屏蔽WEB的JAVA配置,因为此处的TCP启动在SpringBoot之后,在使用容器部署时会出现连接超时
        /*try {
            createTcpServer("9092").start();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
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
        cachingHttpHeadersFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/api/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * 静态文件路径映射(如果不走数据库,那么这个将会使API映射到绝对路径下)
     *
     * @param registry 注册中心
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/META-INF/resources/", "classpath:/resources/",
                "classpath:/static/", "classpath:/public/", "/")
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS));// 设置缓存时间
        registry.addResourceHandler("/api/file/**")
                .addResourceLocations("file:" + attachmentService.getSystemPath() + File.separator)
                .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS));
    }
}
