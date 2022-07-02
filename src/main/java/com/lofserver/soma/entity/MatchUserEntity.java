package com.lofserver.soma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "match_user")
public class MatchUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchUserId;

    @ManyToOne
    @JoinColumn(name = "matchId")
    private MatchLckEntity matchLckEntity;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userEntity;

    public MatchUserEntity(Long matchUserId, MatchLckEntity matchLckEntity, UserEntity userEntity) {
        this.matchUserId = matchUserId;
        this.matchLckEntity = matchLckEntity;
        this.userEntity = userEntity;
    }
}
