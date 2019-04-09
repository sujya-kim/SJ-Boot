package com.sujya.prj.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.sujya.prj.jwt")
public class JwtSettings {
    private Integer tokenExpiredTime;
    private Integer refreshTokenExpTime;
    private String tokenIssuer;
    private String tokenSigningKey;
}
