package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamVsTeam {
    private List<TeamVsTeamSetInfo> setInfoList = new ArrayList<>();

    public TeamVsTeam(List<TeamVsTeamSetInfo> setInfoList) {
        this.setInfoList = setInfoList;
    }
}
