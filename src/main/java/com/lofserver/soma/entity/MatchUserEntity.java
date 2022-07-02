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
    private MatchLckEntity matchId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity userId;

    public MatchUserEntity(Long matchUserId, MatchLckEntity matchId, UserEntity userId) {
        this.matchUserId = matchUserId;
        this.matchId = matchId;
        this.userId = userId;
    }
}
