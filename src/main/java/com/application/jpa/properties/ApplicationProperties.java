package com.application.jpa.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final RemoteDam remoteDam = new RemoteDam();

    private final FileProperties file = new FileProperties();

    private final LogbackProperties logback = new LogbackProperties();

    @Data
    @Component
    public static class RemoteDam {
        private String username = "admin";
        private String password = "admin";
        private String apiBaseUrl = "www.baidu.com";
    }

    @Data
    @Component
    public static class FileProperties {
        private String uploadPath = "/files";
        private String downLoadPath = "/files";
    }

    @Data
    @Component
    public static class LogbackProperties {
        private String logPath = "./logs";
    }
}
