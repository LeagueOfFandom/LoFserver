package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class LeagueList {
    private String league;
    private List<TeamList> teamList;
}
