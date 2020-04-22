package com.application.service.dto;

import com.application.domain.enumeration.IMType;
import lombok.Getter;
import lombok.Setter;

/**
 * Websocket 消息数据
 */
@Setter
@Getter
public class IMDto {
    private Long conferenceId;
    private IMType type;
    private String content;
    private Long participantId;
}
