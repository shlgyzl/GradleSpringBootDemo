package com.application.design_pattern.bridge_pattern.dimension;

import java.util.List;

public class WechatMsgSender implements MsgSender {
    private List<String> wechats;

    public WechatMsgSender(List<String> wechats) {
        this.wechats = wechats;
    }

    @Override
    public void send(String message) {
        //...
    }
}
