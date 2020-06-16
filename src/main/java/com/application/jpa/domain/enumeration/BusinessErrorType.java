package com.application.jpa.domain.enumeration;

public enum BusinessErrorType {
    PARAMETER_EXCEPTION(102, "参数异常!"),
    SERVICE_TIME_OUT(103, "服务调用超时！"),
    PARAMETER_BIG_EXCEPTION(102, "输入的图片数量不能超过50张!"),
    UNEXPECTED_EXCEPTION(500, "系统发生异常，请联系管理员！");
    private int code;
    private String msg;

    BusinessErrorType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
