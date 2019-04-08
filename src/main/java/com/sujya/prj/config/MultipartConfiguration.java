package com.sujya.prj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(value = "file")
public class MultipartConfiguration {
    private String uploadDir;

}
