package com.application.web.service.dto;

import com.application.web.domain.jpa.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Role RoleDTO;

}