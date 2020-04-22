package com.application.web.resources.exception;

import com.application.domain.enumeration.BusinessErrorType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class BusinessErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;

    public BusinessErrorException(BusinessErrorType type) {
        this.code = type.getCode();
        this.msg = type.getMsg();
    }
}
