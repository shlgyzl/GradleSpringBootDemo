package com.application.feign.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FeignClientException extends RuntimeException {

    private Integer status;

    public FeignClientException(String message, Integer status) {
        super(message);
        this.status=status;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }


    @Data
    @Accessors(chain = true)
    public static class Message {
        /**
         * 消息
         */
        private String message;

        /**
         * 描述
         */
        private String description;

        /***
         * 字段error
         */
        private String fieldErrors;

        /**
         * 返回状态
         */
        private Integer status=400;
    }
}

