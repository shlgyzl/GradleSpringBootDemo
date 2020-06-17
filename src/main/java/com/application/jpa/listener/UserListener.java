package com.application.jpa.listener;

import com.application.jpa.common.event.UserEvent;
import com.application.jpa.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

/**
 * 自定义监听器,用于处理业务
 *
 * @author yanghaiyong
 */
@Slf4j
public class UserListener implements ApplicationListener<UserEvent> {

    @Override
    public void onApplicationEvent(UserEvent event) {
        User user = event.getUser();
        log.info("处理其他事务{}", user.toString());
    }
}
