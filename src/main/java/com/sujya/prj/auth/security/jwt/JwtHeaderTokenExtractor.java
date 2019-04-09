package com.sujya.prj.auth.security.jwt;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {

    public static final String HEADER_PREFIX_SJ = "SJ";
    public static final String HEADER_PREFIX_BEARER = "Bearer";
    @Override
    public String extract(String payload) {

        if(payload.length() < HEADER_PREFIX_SJ.length()){
            throw new AuthenticationServiceException("Invalid authorization header size");
        }

        if(payload.startsWith(HEADER_PREFIX_SJ)){
            return payload.substring(HEADER_PREFIX_SJ.length(), payload.length());
        }else{
            return payload.substring(HEADER_PREFIX_BEARER.length(), payload.length());
        }
    }

    @Override
    public String getHeader(String payload) {
        if(payload.startsWith(HEADER_PREFIX_SJ)){
            return HEADER_PREFIX_SJ;
        }else{
            return HEADER_PREFIX_BEARER;
        }
    }
}
