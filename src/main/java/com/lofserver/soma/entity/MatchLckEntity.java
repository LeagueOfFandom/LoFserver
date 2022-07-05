package com.lofserver.soma.entity;


import com.lofserver.soma.controller.v1.response.match.Match;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "match_lck")
public class MatchLckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @ManyToOne
    @JoinColumn(name = "homeId")
    private TeamEntity homeId;

    @ManyToOne
    @JoinColumn(name = "awayId")
    private TeamEntity awayId;

    @Column
    private LocalDate matchDate;

    @Column
    private LocalTime matchTime;

    @Column
    private Long homeScore;

    @Column
    private Long awayScore;

    @OneToMany(mappedBy = "matchUserId")
    private List<MatchUserEntity> matchUserEntityList;
    public MatchLckEntity(Long matchId, TeamEntity homeId, TeamEntity awayId, LocalDate matchDate, LocalTime matchTime, Long homeScore, Long awayScore) {
        this.matchId = matchId;
        this.homeId = homeId;
        this.awayId = awayId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public Match toMatch(Boolean live, String link, Boolean alarm){
        return new Match(this.matchId,this.matchDate,this.matchTime,this.homeId.getTeamName(),this.homeScore,this.homeId.getTeamImg(),this.awayId.getTeamName(),this.awayScore,this.awayId.getTeamImg(),live,link,alarm);
    }
}
