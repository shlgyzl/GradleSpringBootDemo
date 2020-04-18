package com.application.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "web.application.file", ignoreUnknownFields = false)
@Component
public class FileApplicationProperties {
    private String path;
}
