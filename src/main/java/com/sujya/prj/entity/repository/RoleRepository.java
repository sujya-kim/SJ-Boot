package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.RoleEntity;
import com.sujya.prj.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    List<RoleEntity> findAll();

    Optional<RoleEntity> findByRoleId(int roleId);
    RoleEntity findByRoleNm(String roleNm);

}
