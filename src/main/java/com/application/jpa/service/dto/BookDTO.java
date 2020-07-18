package com.application.jpa.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yanghaiyong
 * 2020/7/13-20:19
 */
@Data
public class BookDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String login;
    private String name;
    private Long count;

    public BookDTO(String login, String name, Long count) {
        this.login = login;
        this.name = name;
        this.count = count;
    }
}
