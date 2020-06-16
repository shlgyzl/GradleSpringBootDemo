package com.application.jpa.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Component
@Getter
public class ApplicationProperties {

    private final RemoteDam remoteDam = new RemoteDam();

    private final FileProperties file = new FileProperties();

    @Data
    @Component
    public static class RemoteDam {
        private String username;
        private String password;
        private String apiBaseUrl;
    }

    @Data
    @Component
    public static class FileProperties {
        private String uploadPath = "/files";
        private String downLoadPath = "/files";
    }
}
