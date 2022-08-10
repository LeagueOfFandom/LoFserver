package com.lofserver.soma.controller.v1.response.teamRank;

import com.lofserver.soma.entity.TeamRankingEntity;
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
    private String seriesWin;

    private String seriesLose;

    private String seriesWinRate;

    private String gamesWin;

    private String gamesLose;

    private String gamesWinRate;

    private Long points;

    private List<String> recentMatches;

    public TeamRanking(TeamRankingEntity teamRankingEntity, String name, String teamImg, List<String> recentMatches) {
        this.name = name;
        this.teamImg = teamImg;
        this.recentMatches = recentMatches;

        this.year = teamRankingEntity.getYear();
        this.season = teamRankingEntity.getSeason();
        this.league = teamRankingEntity.getLeague();
        this.rank = teamRankingEntity.getRank();
        this.seriesWin = teamRankingEntity.getSeriesWin();
        this.seriesLose = teamRankingEntity.getSeriesLose();
        this.seriesWinRate = teamRankingEntity.getSeriesWinRate();
        this.gamesWin = teamRankingEntity.getGamesWin();
        this.gamesLose = teamRankingEntity.getGamesLose();
        this.gamesWinRate = teamRankingEntity.getGamesWinRate();
        this.points = teamRankingEntity.getPoints();
    }
}
