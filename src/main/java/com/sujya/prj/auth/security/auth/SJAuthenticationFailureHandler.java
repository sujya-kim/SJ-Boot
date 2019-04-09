package com.sujya.prj.auth.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sujya.prj.auth.common.ErrorCode;
import com.sujya.prj.auth.common.ErrorResponse;
import com.sujya.prj.auth.common.JwtExpiredTokenException;
import com.sujya.prj.auth.security.exception.AuthMethodNotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SJAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper;

    @Autowired
    public SJAuthenticationFailureHandler(ObjectMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED);

        if(exception instanceof BadCredentialsException){
            errorResponse = ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }else if(exception instanceof JwtExpiredTokenException) {
            errorResponse = ErrorResponse.of("Jwt token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
        }else if(exception instanceof AuthMethodNotSupportedException) {
            errorResponse = ErrorResponse.of(exception.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        mapper.writeValue(response.getWriter(), errorResponse);
    }
}
