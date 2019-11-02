package com.application.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class H2DataBaseConfiguration {
    private final Logger log = LoggerFactory.getLogger(H2DataBaseConfiguration.class);
    private final Environment env;

    public H2DataBaseConfiguration(Environment env) {
        this.env = env;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Object h2TCPServer() {
        String port = getValidPortForH2();
        log.debug("H2 database is available on port {}", port);

        return null;
    }

    private String getValidPortForH2() {
        int port = Integer.parseInt(env.getProperty("server.port"));
        while (port < 10000 || port > 63536 - 2000) {
            if (port < 10000) {
                port += 2000;
            } else {
                port -= 2000;
            }
        }
        return String.valueOf(port);
    }
}
