package com.sujya.prj.auth.security.jwt;

public interface TokenExtractor {
    String extract(String payload);
    String getHeader(String payload);
}
