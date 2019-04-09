package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String username);

}
