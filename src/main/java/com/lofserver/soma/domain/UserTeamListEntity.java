package com.lofserver.soma.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "user_teamlist_table")
public class UserTeamListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTeamlistId;
    @Column
    private Long userId;
    @Column
    private Long teamId;

    public UserTeamListEntity(Long userTeamlistId, Long userId, Long teamId) {
        this.userTeamlistId = userTeamlistId;
        this.userId = userId;
        this.teamId = teamId;
    }
}
