package com.lofserver.soma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column
    private String token;

    @OneToMany(mappedBy = "userEntity")
    private List<TeamUserEntity> teamUserEntityList;

    public UserEntity(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}