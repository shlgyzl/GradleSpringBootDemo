package com.application.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {
    private static final long serialVersionUID = 6826329547782771603L;
    private String imageUrl;

    public SecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String imageUrl) {
        super(username, password, authorities);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
