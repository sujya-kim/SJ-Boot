package com.sujya.prj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="ROLE_TB")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RoleEntity {

    @Id
    @Column(name="ROLE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name="ROLE_NAME", nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

}
