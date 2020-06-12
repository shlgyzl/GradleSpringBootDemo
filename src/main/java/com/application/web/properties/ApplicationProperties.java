package com.application.web.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Component
@Getter
public class ApplicationProperties {
    private final RemoteDam remoteDam = new RemoteDam();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RemoteDam {
        private String username;
        private String password;
        private String apiBaseUrl;
    }
}
