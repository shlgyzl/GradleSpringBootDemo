package com.application.web.resources.controller.index;

import com.application.web.domain.enumeration.AuthType;
import com.application.security.config.jwt.JWTConfigurer;
import com.application.security.config.jwt.TokenProvider;
import com.application.security.config.token.RemoteUsernamePasswordAuthenticationToken;
import com.application.web.resources.vm.LoginVM;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(value = "UserJWT用户认证管理接口", tags = {"UserJWT用户认证管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class UserJWTController {
    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    @ApiOperationSupport
    @ApiOperation(value = "用户登录", notes = "无条件限制")
    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorizeByLocalPassword(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        return authorize(authenticationToken, Optional.ofNullable(loginVM.getRememberMe()).orElse(false), AuthType.LOCAL_USERNAME);
    }

    @ApiOperationSupport
    @ApiOperation(value = "用户登录(用户远程登录)", notes = "条件限制(用户远程登录)")
    @PostMapping("/authenticate/remote")
    @Timed
    public ResponseEntity<JWTToken> authorizeByRemotePassword(@Valid @RequestBody LoginVM loginVM) {
        RemoteUsernamePasswordAuthenticationToken authenticationToken
                = new RemoteUsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
        return authorize(authenticationToken, Optional.ofNullable(loginVM.getRememberMe()).orElse(false), AuthType.REMOTE_USERNAME);
    }

    private ResponseEntity<JWTToken> authorize(AbstractAuthenticationToken authenticationToken, boolean rememberMe, AuthType authType) {
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, rememberMe, authType);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    private static class JWTToken {
        @JsonProperty("id_token")
        private String idToken;
    }
}
