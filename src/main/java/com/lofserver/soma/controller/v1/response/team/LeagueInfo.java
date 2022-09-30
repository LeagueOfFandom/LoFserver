package com.lofserver.soma.controller.v1.response.team;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LeagueInfo {
    private String note;
    private List<TeamInfo> teamInfo;

    @Builder
    public LeagueInfo(String note, List<TeamInfo> teamInfo) {
        this.note = note;
        this.teamInfo = teamInfo;
    }
}
