package com.application.common.event;

import com.application.web.domain.jpa.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * 自定义事件
 *
 * @author yanghaiyong
 */
@Getter
@Setter
public class UserEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;
    private User user;

    public UserEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
