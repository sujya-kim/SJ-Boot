package com.sujya.prj.auth.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sujya.prj.auth.common.JwtToken;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class AccessJwtToken implements JwtToken {
    @Setter(AccessLevel.NONE)
    private final String token;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    private final Claims claims;
}
