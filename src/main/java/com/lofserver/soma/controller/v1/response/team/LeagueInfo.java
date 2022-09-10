package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class LeagueInfo {
    private String league;
    private List<TeamInfo> teamInfo;

    public LeagueInfo(String league) {
        this.league = league;
    }

    public void setTeamInfo(List<TeamInfo> teamInfo) {
        this.teamInfo = teamInfo;
    }
}
