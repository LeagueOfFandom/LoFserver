package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

@Getter
public class TeamVsTeam {
    private String homeTeam;
    private Long homeWinGame;
    private Long homeWinSet;
    private String awayTeam;
    private Long awayWinGame;
    private Long awayWinSet;

    public TeamVsTeam(String homeTeam, Long homeWinGame, Long homeWinSet, String awayTeam, Long awayWinGame, Long awayWinSet) {
        this.homeTeam = homeTeam;
        this.homeWinGame = homeWinGame;
        this.homeWinSet = homeWinSet;
        this.awayTeam = awayTeam;
        this.awayWinGame = awayWinGame;
        this.awayWinSet = awayWinSet;
    }
}
