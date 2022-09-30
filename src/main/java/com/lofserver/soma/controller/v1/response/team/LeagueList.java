package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class LeagueList {
    private List<LeagueInfo> leagueInfo;
    private List<String> leagueList;

    public LeagueList(List<LeagueInfo> leagueInfo, List<String> leagueList) {
        this.leagueInfo = leagueInfo;
        this.leagueList = leagueList;
    }
}
