package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class LeagueInfo {
    private String note;
    private List<TeamInfo> teamInfo;

    public LeagueInfo(String note) {
        this.note = note;
    }

    public void setTeamInfo(List<TeamInfo> teamInfo) {
        this.teamInfo = teamInfo;
    }
}
