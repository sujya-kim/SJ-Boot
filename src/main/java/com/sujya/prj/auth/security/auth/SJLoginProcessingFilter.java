package com.sujya.prj.auth.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sujya.prj.auth.common.JwtAuthenticationToken;
import com.sujya.prj.auth.common.RawAccessJwtToken;
import com.sujya.prj.auth.config.WebSecurityConfig;
import com.sujya.prj.auth.security.exception.AuthMethodNotSupportedException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SJLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;
    private final String defaultProcessUrl;

    public SJLoginProcessingFilter(String defaultFilterProcessesUrl,
                                      AuthenticationSuccessHandler successHandler,
                                      AuthenticationFailureHandler failureHandler,
                                      ObjectMapper mapper) {
        super(defaultFilterProcessesUrl);
        this.defaultProcessUrl = defaultFilterProcessesUrl;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.objectMapper = mapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if(defaultProcessUrl.equals("/auth/login")){
            if(!HttpMethod.POST.name().equals(request.getMethod())){
                throw new AuthMethodNotSupportedException("Authentication method not supported");
            }

            LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

            String encPassword = new BCryptPasswordEncoder().encode(loginRequest.getPassword());

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()
            );
            return this.getAuthenticationManager().authenticate(token);
        }else{
            String tokenPayload = request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM);
            tokenPayload = tokenPayload.substring("Bearer".length());
            RawAccessJwtToken token = new RawAccessJwtToken(tokenPayload);
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) getAuthenticationManager()
                    .authenticate(new JwtAuthenticationToken(token));
            return authentication;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws  IOException, ServletException{
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws  IOException, ServletException{

        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

}
