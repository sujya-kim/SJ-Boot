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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID", nullable = false)
    private String id;

    @Column(name="NAME", nullable = false)
    private String name;

    @Column(name="PASSWORD", nullable = false)
    private String password;

    @Column(name="USER_NAME", nullable = false)
    private String username;

    @Column(name="ROLE_ID", nullable = false)
    private Integer rolId;

    public UserEntity(String name, String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
