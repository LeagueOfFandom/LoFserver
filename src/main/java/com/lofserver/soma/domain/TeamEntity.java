package com.lofserver.soma.domain;


import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "team_table")
@ToString
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

    public TeamEntity(Long teamId, Long leagueId, String teamName, String teamImg) {
        this.teamId = teamId;
        this.teamLeague = leagueId;
        this.teamName = teamName;
        this.teamImg = teamImg;
    }
}
