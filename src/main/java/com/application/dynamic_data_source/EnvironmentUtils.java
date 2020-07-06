package com.application.dynamic_data_source;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author yanghaiyong
 * 2020/7/2-21:02
 */
@Component
public class EnvironmentUtils implements EnvironmentAware {
    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        EnvironmentUtils.environment = environment;
    }

    public static Environment getEnvironment() {
        return EnvironmentUtils.environment;
    }
}
