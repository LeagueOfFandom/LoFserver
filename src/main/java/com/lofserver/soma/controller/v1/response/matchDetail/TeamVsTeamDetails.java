package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

import java.util.List;

@Getter
public class TeamVsTeamDetails {
    private List<TeamVsTeamSetInfo> teamVsTeamSetInfo;
    private TeamVsTeamRosterInfo teamVsTeamRosterInfo;

    private TeamVsTeamMainInfo teamVsTeamMainInfo;
    private TeamVsTeamPrediction teamVsTeamPrediction;

    public TeamVsTeamDetails(List<TeamVsTeamSetInfo> teamVsTeamSetInfo, TeamVsTeamRosterInfo teamVsTeamRosterInfo, TeamVsTeamMainInfo teamVsTeamMainInfo, TeamVsTeamPrediction teamVsTeamPrediction) {
        this.teamVsTeamSetInfo = teamVsTeamSetInfo;
        this.teamVsTeamRosterInfo = teamVsTeamRosterInfo;
        this.teamVsTeamMainInfo = teamVsTeamMainInfo;
        this.teamVsTeamPrediction = teamVsTeamPrediction;
    }
}
