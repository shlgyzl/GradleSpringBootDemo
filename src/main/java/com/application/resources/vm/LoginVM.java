package com.application.resources.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 接受参数定义的实体类,仅仅只限于接受参数
 */
@Data
public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
    @NotNull
    @Size(min = 1, max = 50)
    private String password;
    private Boolean rememberMe;
}
