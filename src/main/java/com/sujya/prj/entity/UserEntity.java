package com.sujya.prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="USER_TB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserEntity {

    @Id
    @Column(name="USER_ID", nullable = false)
    private String userId;

    @Column(name="PASSWORD", nullable = false)
    private String password;

    @Column(name="USER_NAME", nullable = false)
    private String userName;

    @Column(name="USER_ROLE", nullable = false)
    private Integer userRole;

    @ManyToMany
    private Set<RoleEntity> roles;

}
