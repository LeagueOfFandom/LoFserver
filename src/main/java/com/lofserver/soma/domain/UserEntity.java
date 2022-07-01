package com.lofserver.soma.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_table")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column
    private String deviceId;

    public UserEntity(Long userId, String deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
    }
}
