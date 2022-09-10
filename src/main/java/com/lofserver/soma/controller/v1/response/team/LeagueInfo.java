package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class LeagueInfo {
    private String league;
    private List<TeamList> teamList;

    public LeagueInfo(String league) {
        this.league = league;
    }

    public void setTeamList(List<TeamList> teamList) {
        this.teamList = teamList;
    }
}
