package com.sujya.prj.auth.security.model;

import com.sujya.prj.auth.common.JwtToken;
import com.sujya.prj.auth.common.UserContext;
import com.sujya.prj.auth.config.JwtSettings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenFactory {

    private final JwtSettings jwtSettings;

    @Autowired
    public JwtTokenFactory(JwtSettings jwtSettings){
        this.jwtSettings = jwtSettings;
    }

    public AccessJwtToken createAccessJwtToken(UserContext userContext){

        Claims claims = Jwts.claims().setSubject(userContext.getUserId());
        List<String> scopes = userContext.getAuthrorities().stream().map(s->s.toString())
                .collect(Collectors.toList());
        claims.put("scopes", scopes);

        LocalDateTime currentTime = LocalDateTime.now();
        Date issuedAt = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
        Date expiration = Date.from(currentTime.plusMinutes(jwtSettings.getTokenExpiredTime())
                                    .atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtSettings.getTokenIssuer())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSettings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshJwtToken(UserContext userContext){
        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUserId());
        claims.put("scopes", Arrays.asList("refresh_token"));

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtSettings.getTokenIssuer())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(jwtSettings.getRefreshTokenExpTime()).atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, jwtSettings.getTokenSigningKey())
                .compact();

        return new AccessJwtToken(token, claims);

    }
}
