package com.sujya.prj.service;

import com.sujya.prj.entity.UserEntity;

public interface UserService {

    void save(UserEntity user);

    UserEntity findByUsername(String username);

}
