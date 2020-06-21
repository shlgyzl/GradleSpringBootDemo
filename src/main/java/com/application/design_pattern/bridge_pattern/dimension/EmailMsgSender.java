package com.application.design_pattern.bridge_pattern.dimension;

import java.util.List;

public class EmailMsgSender implements MsgSender {
    private List<String> emails;

    public EmailMsgSender(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public void send(String message) {
        //...
    }
}
