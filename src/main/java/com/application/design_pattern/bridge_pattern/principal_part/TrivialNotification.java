package com.application.design_pattern.bridge_pattern.principal_part;

import com.application.design_pattern.bridge_pattern.dimension.MsgSender;

public class TrivialNotification extends Notification {
    public TrivialNotification(MsgSender msgSender) {
        super(msgSender);
    }

    @Override
    public void notify(String message) {

    }
}
