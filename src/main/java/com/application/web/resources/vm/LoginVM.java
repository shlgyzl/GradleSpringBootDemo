package com.application.web.resources.vm;

import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 接受参数定义的实体类,仅仅只限于接受参数
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class LoginVM implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(name = "username", value = "用户名", required = true, dataType = "String", example = "admin", notes = "字符不能大于50个")
    private String username;

    @NotNull
    @Size(min = 1, max = 50)
    @ApiModelProperty(name = "password", value = "用户名", required = true, dataType = "String", example = "admin", notes = "字符不能大于50个")
    private String password;

    @ApiModelProperty(name = "rememberMe", value = "是否记住我", required = true, dataType = "Boolean", example = "false")
    private Boolean rememberMe = false;
}
