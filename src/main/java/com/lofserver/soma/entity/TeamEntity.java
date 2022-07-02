package com.lofserver.soma.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "team")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;
    @Column
    private Long teamLeague;
    @Column
    private String teamName;
    @Column
    private String teamImg;

    @OneToMany(mappedBy = "teamEntity")
    private List<TeamUserEntity> teamUserEntityList;
    public TeamEntity(Long teamId, Long leagueId, String teamName, String teamImg) {
        this.teamId = teamId;
        this.teamLeague = leagueId;
        this.teamName = teamName;
        this.teamImg = teamImg;
    }
}
