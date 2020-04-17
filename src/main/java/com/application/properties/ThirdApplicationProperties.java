package com.application.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "third.application.user", ignoreUnknownFields = false)
@Component
public class ThirdApplicationProperties {
    private String username;
    private String password;
    private String apiBaseUrl;
}
