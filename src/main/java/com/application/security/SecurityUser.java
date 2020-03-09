package com.application.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
public class SecurityUser extends User {
    private static final long serialVersionUID = 6826329547782771603L;
    private String imageUrl;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String imageUrl){
        super(username,password,authorities);
        this.imageUrl = imageUrl;
    }
}
