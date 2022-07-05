package com.lofserver.soma.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "team_user")
public class TeamUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamUserId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "teamId")
    private TeamEntity teamEntity;

    public TeamUserEntity(Long teamUserId, UserEntity userEntity, TeamEntity teamEntity) {
        this.teamUserId = teamUserId;
        this.userEntity = userEntity;
        this.teamEntity = teamEntity;
    }
}
