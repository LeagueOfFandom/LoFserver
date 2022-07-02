package com.lofserver.soma.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_select")
public class UserSelectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSelectId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @Column
    private Long matchId;

    @Column
    private Boolean alarm;

    public UserSelectEntity(Long userSelectId, UserEntity userEntity, Long matchId, Boolean alarm) {
        this.userSelectId = userSelectId;
        this.userEntity = userEntity;
        this.matchId = matchId;
        this.alarm = alarm;
    }

    public void setAlarm(Boolean alarm) {
        this.alarm = alarm;
    }
}
