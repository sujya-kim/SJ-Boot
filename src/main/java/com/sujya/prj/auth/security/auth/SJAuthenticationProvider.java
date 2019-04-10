package com.sujya.prj.auth.security.auth;

import com.sujya.prj.auth.common.UserContext;
import com.sujya.prj.entity.RoleEntity;
import com.sujya.prj.entity.UserEntity;
import com.sujya.prj.entity.repository.RoleRepository;
import com.sujya.prj.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SJAuthenticationProvider implements AuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public SJAuthenticationProvider(final UserRepository userRepository, final RoleRepository roleRepository, final BCryptPasswordEncoder encoder){
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String usrId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        return authenticate(usrId, password);
    }

    public Authentication authenticate(String usrId, String password) throws  AuthenticationException{

        UserEntity userEntity = userRepository.findByUsername(usrId)
                .orElseThrow(()->{
                    return new UsernameNotFoundException("User not found");
                });

        String expectedPassword = encoder.encode(password);

        if(!encoder.matches(password, userEntity.getPassword())){
            throw new BadCredentialsException("Authentication failed, Username or password not valid");
        }

        if(userEntity.getRolId() == null){
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }

        RoleEntity roleEntity = roleRepository.findByRoleId(userEntity.getRolId()).get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleEntity.getRoleNm()));

        UserContext userContext = UserContext.create(userEntity.getUsername(), authorities);

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthrorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
