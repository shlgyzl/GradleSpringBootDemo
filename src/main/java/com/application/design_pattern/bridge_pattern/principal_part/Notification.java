package com.application.design_pattern.bridge_pattern.principal_part;

import com.application.design_pattern.bridge_pattern.dimension.MsgSender;

/**
 * 抽象体
 *
 * @author yanghaiyong
 */
public abstract class Notification {
    protected MsgSender msgSender;

    public Notification(MsgSender msgSender) {
        this.msgSender = msgSender;
    }

    public abstract void notify(String message);
}
