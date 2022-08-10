package com.lofserver.soma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "team_ranking")
public class TeamRankingEntity {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name="year")
    private String year;

    @Column(name="season")
    private String season;

    @Column(name="league")
    private String league;

    @Column(name = "rank")
    private Long rank;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "series_win")
    private String seriesWin;

    @Column(name = "series_lose")
    private String seriesLose;

    @Column(name = "series_win_rate")
    private String seriesWinRate;

    @Column(name="games_win")
    private String gamesWin;

    @Column(name="games_lose")
    private String gamesLose;

    @Column(name="games_win_rate")
    private String gamesWinRate;

    @Column(name="points")
    private Long points;

}
