package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findAll();

    UserEntity findByUserName(String userName);

}
