package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamVsTeam {
    private List<TeamVsTeamDetails> setInfoList = new ArrayList<>();

    public TeamVsTeam(List<TeamVsTeamDetails> setInfoList) {
        this.setInfoList = setInfoList;
    }
}
