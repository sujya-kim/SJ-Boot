package com.sujya.prj.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sujya.prj.auth.CustomCorsFilter;
import com.sujya.prj.auth.security.RestAuthenticationEntryPoint;
import com.sujya.prj.auth.security.auth.SJAuthenticationFailureHandler;
import com.sujya.prj.auth.security.auth.SJAuthenticationProvider;
import com.sujya.prj.auth.security.auth.SJAuthenticationSuccessHandler;
import com.sujya.prj.auth.security.auth.SJLoginProcessingFilter;
import com.sujya.prj.auth.security.jwt.JwtAuthenticationProvider;
import com.sujya.prj.auth.security.jwt.JwtTokenAuthenticationProcessingFilter;
import com.sujya.prj.auth.security.jwt.SkipPathRequestMatcher;
import com.sujya.prj.auth.security.jwt.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";

    public static final String FORM_BASED_LOGIN_ENTRY_POINT="/auth/login";
    public static final String TOKEN_REFRESH_ENTRY_POINT="/auth/token";

    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";

    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private SJAuthenticationSuccessHandler successHandler;

    @Autowired
    private SJAuthenticationFailureHandler failureHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TokenExtractor tokenExtractor;

    @Autowired
    private SJAuthenticationProvider sjAuthenticationProvider;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(sjAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    protected SJLoginProcessingFilter buildSJLoginProcessingFilter() throws Exception{
        SJLoginProcessingFilter filter = new SJLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected SJLoginProcessingFilter refreshSJLoginProcessingFilter() throws Exception{
        SJLoginProcessingFilter filter = new SJLoginProcessingFilter(TOKEN_REFRESH_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception{
        List<String> pathsToSkip = Arrays.asList(FORM_BASED_LOGIN_ENTRY_POINT, TOKEN_REFRESH_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);

        return filter;
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception{

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
                        .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
                .and()
                    .authorizeRequests()
                        .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
                .and()
                    .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(buildSJLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(refreshSJLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
