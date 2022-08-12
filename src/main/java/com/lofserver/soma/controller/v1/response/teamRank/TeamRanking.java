package com.lofserver.soma.controller.v1.response.teamRank;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamRanking {

    private String year;

    private String season;

    private String league;

    private Long rank;

    private String name; //규격화된 팀 이름

    private String teamImg;

    private String seriesWinLose;

    private String seriesWinRate;

    private String gamesWinLose;

    private String gamesWinRate;

    private Long points;

    private List<String> recentMatches;

}
