package com.application.jpa.domain.enumeration;

/**
 * 身份认证类型
 */
public enum AuthType {
    /**
     * 本地认证
     */
    LOCAL_USERNAME,
    /**
     * 远程认证
     */
    REMOTE_USERNAME,
    /**
     * 远程第三方应用认证
     */
    REMOTE_UNION_ID,
    /**
     * 远程手机用户认证
     */
    REMOTE_PHONE,
    /**
     * 外宾手机用户认证
     */
    OUTER_PHONE
}
