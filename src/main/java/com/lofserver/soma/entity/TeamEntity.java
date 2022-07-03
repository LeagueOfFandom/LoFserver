package com.lofserver.soma.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "team")
@ToString
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    @Column
    private Long leagueId;
    @Column
    private String teamName;
    @Column
    private String teamImg;

    @OneToMany(mappedBy = "teamEntity")
    private List<TeamUserEntity> teamUserEntityList;

    @OneToMany(mappedBy = "homeId")
    private List<MatchLckEntity> homeMatchLckEntityList;

    @OneToMany(mappedBy = "awayId")
    private List<MatchLckEntity> awayMatchLckEntityList;
    public TeamEntity(Long teamId, Long leagueId, String teamName, String teamImg) {
        this.teamId = teamId;
        this.leagueId = leagueId;
        this.teamName = teamName;
        this.teamImg = teamImg;
    }
}
