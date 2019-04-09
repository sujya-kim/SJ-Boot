package com.sujya.prj.auth.security.jwt;

import com.sujya.prj.auth.common.JwtAuthenticationToken;
import com.sujya.prj.auth.common.RawAccessJwtToken;
import com.sujya.prj.auth.common.UserContext;
import com.sujya.prj.auth.config.JwtSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings){
        this.jwtSettings = jwtSettings;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        RawAccessJwtToken rawAccessJwtToken = (RawAccessJwtToken) authentication.getCredentials();

        Jws<Claims> jwsclaims = rawAccessJwtToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsclaims.getBody().getSubject();
        List<String> scopes = jwsclaims.getBody().get("scopes", List.class);

        List<GrantedAuthority> authorities = scopes.stream().map(authority->{
            return new SimpleGrantedAuthority(authority);
        }).collect(Collectors.toList());

        UserContext userContext = UserContext.create(subject, authorities);

        return new JwtAuthenticationToken(userContext, userContext.getAuthrorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
