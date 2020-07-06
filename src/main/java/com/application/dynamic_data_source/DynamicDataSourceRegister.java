package com.application.dynamic_data_source;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * 动态数据源注册
 *
 * @author yanghaiyong
 * 2020/7/2-20:51
 */
public class DynamicDataSourceRegister implements EnvironmentAware {
    @Override
    public void setEnvironment(Environment environment) {
        DynamicDataSourceRegisterUtil.initAndRegisterDataSource(environment);
    }
}
