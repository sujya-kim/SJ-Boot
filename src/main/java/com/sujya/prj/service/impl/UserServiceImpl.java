package com.sujya.prj.service.impl;

import com.sujya.prj.entity.UserEntity;
import com.sujya.prj.entity.repository.UserRepository;
import com.sujya.prj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(UserEntity user) {

        String pw = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(pw));
        userRepository.save(user);

    }


    @Override
    public UserEntity findByUsername(String username) {

        return userRepository.findByUserName(username);
    }


}
