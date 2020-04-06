package com.application.listener;

import com.application.domain.jpa.User;

import javax.persistence.PostPersist;

public class UserAuditListener {
    @PostPersist
    private void postSave(User user) {
        System.out.println("用户保存之后");
    }
}
