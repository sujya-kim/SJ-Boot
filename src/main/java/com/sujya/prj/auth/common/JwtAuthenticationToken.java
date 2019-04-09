package com.sujya.prj.auth.common;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private RawAccessJwtToken rawAccessJwtToken;
    private UserContext userContext;

    public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
        super(null);
        this.rawAccessJwtToken = unsafeToken;
        this.setAuthenticated(false);
    }
    public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return rawAccessJwtToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void setAuthenticated(boolean authenticated){
        if(authenticated){
            throw new IllegalArgumentException("Cannot set this token to trusted");
        }
        super.setAuthenticated(false);
    }
}
