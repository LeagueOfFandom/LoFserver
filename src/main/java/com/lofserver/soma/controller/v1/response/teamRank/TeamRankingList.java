package com.lofserver.soma.controller.v1.response.teamRank;


import lombok.Getter;

import java.util.List;

@Getter
public class TeamRankingList {
    private List<TeamRanking> teamRankingList;
    public TeamRankingList(List<TeamRanking> teamRankingList) {
        this.teamRankingList = teamRankingList;
    }
}
