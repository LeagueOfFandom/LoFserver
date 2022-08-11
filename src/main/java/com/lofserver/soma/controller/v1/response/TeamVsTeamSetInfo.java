package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamVsTeamSetInfo {
    private String blueTeam;
    private String blueTeamImg;
    private String blueTeamWinSet;

    private List<Roster> blueTeamRoster = new ArrayList<>();

    private String redTeam;
    private String redTeamImg;
    private String redTeamWinSet;

    private List<Roster> redTeamRoster = new ArrayList<>();

    private String blueTeamKills;
    private String blueTeamGold;
    private String blueTeamHerald;
    private String blueTeamDragon;
    private String blueTeamBaron;
    private String blueTeamTower;
    private String blueTeamInhibitor;

    private String redTeamKills;
    private String redTeamGold;
    private String redTeamHerald;
    private String redTeamDragon;
    private String redTeamBaron;
    private String redTeamTower;
    private String redTeamInhibitor;

    public TeamVsTeamSetInfo(String blueTeam, String blueTeamImg, String blueTeamWinSet, List<Roster> blueTeamRoster, String redTeam, String redTeamImg, String redTeamWinSet, List<Roster> redTeamRoster, String blueTeamKills, String blueTeamGold, String blueTeamHerald, String blueTeamDragon, String blueTeamBaron, String blueTeamTower, String blueTeamInhibitor, String redTeamKills, String redTeamGold, String redTeamHerald, String redTeamDragon, String redTeamBaron, String redTeamTower, String redTeamInhibitor) {
        this.blueTeam = blueTeam;
        this.blueTeamImg = blueTeamImg;
        this.blueTeamWinSet = blueTeamWinSet;
        this.blueTeamRoster = blueTeamRoster;
        this.redTeam = redTeam;
        this.redTeamImg = redTeamImg;
        this.redTeamWinSet = redTeamWinSet;
        this.redTeamRoster = redTeamRoster;
        this.blueTeamKills = blueTeamKills;
        this.blueTeamGold = blueTeamGold;
        this.blueTeamHerald = blueTeamHerald;
        this.blueTeamDragon = blueTeamDragon;
        this.blueTeamBaron = blueTeamBaron;
        this.blueTeamTower = blueTeamTower;
        this.blueTeamInhibitor = blueTeamInhibitor;
        this.redTeamKills = redTeamKills;
        this.redTeamGold = redTeamGold;
        this.redTeamHerald = redTeamHerald;
        this.redTeamDragon = redTeamDragon;
        this.redTeamBaron = redTeamBaron;
        this.redTeamTower = redTeamTower;
        this.redTeamInhibitor = redTeamInhibitor;
    }
}
