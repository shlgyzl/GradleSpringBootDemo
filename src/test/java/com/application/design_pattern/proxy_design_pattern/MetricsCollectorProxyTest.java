package com.application.design_pattern.proxy_design_pattern;

import org.junit.jupiter.api.Test;

class MetricsCollectorProxyTest {
    @Test
    public void test() {
        MetricsCollectorProxy proxy = new MetricsCollectorProxy();
        IUserService userService = (IUserService) proxy.createProxy((IUserService) () -> System.out.println("你好呀"));
        userService.say();
    }

    public interface IUserService {
        public void say();
    }
}