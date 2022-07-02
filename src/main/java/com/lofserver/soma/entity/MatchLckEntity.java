package com.lofserver.soma.entity;


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
    @JoinColumn(name = "teamId")
    private TeamEntity homeId;

    @ManyToOne
    @JoinColumn(name = "teamId")
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
}
