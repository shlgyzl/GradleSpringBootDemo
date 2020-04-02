package com.application.config;

import com.application.helper.H2ServerStartHelper;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.sql.SQLException;
import java.util.Objects;

//@Configuration
public class H2DataBaseConfiguration {
    private final Logger log = LoggerFactory.getLogger(H2DataBaseConfiguration.class);
    private final Environment env;

    public H2DataBaseConfiguration(Environment env) {
        this.env = env;
    }

    //@Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TCPServer() throws SQLException {
        String port = getValidPortForH2();
        log.debug("H2 database is available on port {}", port);
        // 默认是9092端口,且是WEB方式
        return H2ServerStartHelper.createServer();
    }

    private String getValidPortForH2() {
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("server.port")));
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
