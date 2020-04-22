package com.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String login;
    private final String imageUrl;

}
