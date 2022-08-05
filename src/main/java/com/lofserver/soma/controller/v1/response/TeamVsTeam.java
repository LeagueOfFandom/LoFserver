package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamVsTeam {
    private String homeTeam;
    private Long homeWinGame;
    private Long homeWinSet;
    private String homeTeamImg;
    private String awayTeam;
    private Long awayWinGame;
    private Long awayWinSet;
    private String awayTeamImg;

    private List<Roster> rosterList = new ArrayList<>();

    public TeamVsTeam(String homeTeam, Long homeWinGame, Long homeWinSet, String homeTeamImg, String awayTeam, Long awayWinGame, Long awayWinSet, String awayTeamImg, List<Roster> rosterList) {
        this.homeTeam = homeTeam;
        this.homeWinGame = homeWinGame;
        this.homeWinSet = homeWinSet;
        this.homeTeamImg = homeTeamImg;
        this.awayTeam = awayTeam;
        this.awayWinGame = awayWinGame;
        this.awayWinSet = awayWinSet;
        this.awayTeamImg = awayTeamImg;
        this.rosterList = rosterList;
    }
}
