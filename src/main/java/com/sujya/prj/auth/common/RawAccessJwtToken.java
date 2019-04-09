package com.sujya.prj.auth.common;

import io.jsonwebtoken.*;
import org.springframework.security.authentication.BadCredentialsException;

public class RawAccessJwtToken implements JwtToken {

    private String token;
    private String headerStart;

    public RawAccessJwtToken(String token){
        this.token = token;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    public String getHeaderStart(){
        return this.headerStart;
    }

    public Jws<Claims> parseClaims(String sigingKey) {
        try {
            return Jwts.parser().setSigningKey(sigingKey).parseClaimsJws(this.token);
        }catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex){
            throw new BadCredentialsException("Invalid JWT token:" , ex);
        }catch (ExpiredJwtException exp){
            throw new JwtExpiredTokenException(this, "JWT Token Expired", exp);
        }
    }
}
