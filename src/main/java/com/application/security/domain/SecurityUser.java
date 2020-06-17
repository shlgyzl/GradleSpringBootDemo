package com.application.security.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class SecurityUser extends User {
    private static final long serialVersionUID = 1L;
    private String imageUrl;

    public SecurityUser(String username, String password, boolean enabled,
                        boolean accountNonExpired, boolean credentialsNonExpired,
                        boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                        String imageUrl) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.imageUrl = imageUrl;
    }

    public SecurityUser(String username, String password,
                        Collection<? extends GrantedAuthority> authorities,
                        String imageUrl) {
        super(username, password, authorities);
        this.imageUrl = imageUrl;
    }
}
