package com.neilism.user.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthentication implements Authentication {

    private UserContext userContext;
    private boolean isAuthenticated;

    public UserAuthentication(UserContext userContext, boolean isAuthenticated) {
        this.userContext = userContext;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userContext.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userContext != null ? userContext.getPassword() : null;
    }

    @Override
    public Object getDetails() {
        return userContext;
    }

    @Override
    public Object getPrincipal() {
        return userContext.getUser();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userContext.getUsername() != null ? userContext.getUsername() : (userContext.getUser() != null ? userContext.getUser().getUsername() : null);
    }
}
