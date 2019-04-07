package com.sujya.prj.entity.repository;

import com.sujya.prj.entity.RoleEntity;
import com.sujya.prj.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

    List<RoleEntity> findAll();

    RoleEntity findByRoleId(String roleId);
    RoleEntity findByRoleName(String roleName);

}
