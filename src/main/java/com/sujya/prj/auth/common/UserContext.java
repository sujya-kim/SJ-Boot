package com.sujya.prj.auth.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
public class UserContext {

    @Setter(AccessLevel.NONE)
    private final String userId;

    @Setter(AccessLevel.NONE)
    private final List<GrantedAuthority> authrorities;

    public static UserContext create(String userId, List<GrantedAuthority> authrorities) {
        return new UserContext(userId, authrorities);
    }
}
