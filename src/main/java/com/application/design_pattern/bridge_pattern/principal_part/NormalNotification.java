package com.application.design_pattern.bridge_pattern.principal_part;

import com.application.design_pattern.bridge_pattern.dimension.MsgSender;

public class NormalNotification extends Notification {
    public NormalNotification(MsgSender msgSender) {
        super(msgSender);
    }

    @Override
    public void notify(String message) {
        msgSender.send(message);
    }
}
