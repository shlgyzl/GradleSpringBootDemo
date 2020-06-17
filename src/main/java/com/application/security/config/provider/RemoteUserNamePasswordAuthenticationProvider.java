package com.application.security.config.provider;

import com.application.feign.service.UserServiceFeign;
import com.application.jpa.common.constants.AuthoritiesConstants;
import com.application.security.config.token.RemoteUsernamePasswordAuthenticationToken;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 远程登录校验器
 */
@Component
@Slf4j
public class RemoteUserNamePasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Resource
    private UserServiceFeign userServiceFeign;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            logger.error("远程登录身份验证失败,没有提供有效凭据");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
        String username = authentication.getPrincipal().toString().trim();
        String password = authentication.getCredentials().toString().trim();
        Response response = userServiceFeign.userLogin(username, password);
        if (response.status() != 200) {
            logger.debug("远程登录身份验证失败,用户名或密码错误");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials(),
                user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 此处校验用户名或密码是否正确(因为本地提供的Provider为Dao的,所以此处需要自己提供校验)
        // todo 需要重新校验此处用户名和消息
        return new User(username, (String) authentication.getCredentials(), Collections.singletonList(
                () -> AuthoritiesConstants.USER));
    }

    @Override
    public boolean supports(Class<?> authentication) {// 只有对应的登录Token才能通过
        return RemoteUsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
