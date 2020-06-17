package com.application.feign.service;

import feign.*;

public interface UserServiceFeign {
    /**
     * 登录第三方应用
     *
     * @param username 用户名
     * @param password 密码
     * @return Response
     */
    //@Headers({"Content-Type: application/json", "Accept: application/json"})
    @RequestLine("POST /api/login")
    @Body("j_username={username}&&j_password={password}")
    Response userLogin(@Param("username") String username, @Param("password") String password);
}
