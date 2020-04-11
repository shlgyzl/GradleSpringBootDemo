package com.application.listener;

import com.application.domain.jpa.User;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.PostPersist;

@Slf4j
public class UserAuditListener {
    @PostPersist
    private void postSave(User user) {
        log.info("事件机制启动...,触发保存用户: {},但也可能事务失败！！！", user);
    }
}
