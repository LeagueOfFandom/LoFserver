package com.lofserver.soma.entity.match;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MatchInfo {
    private LocalDate matchDate;
    private LocalTime matchTime;
    private Long homeTeamId;
    private Long awayTeamId;
    private String liveLink;
    private List<String> homeRoster = new ArrayList<>();
    private List<String> awayRoster = new ArrayList<>();

    public void setHomeRoster(List<String> homeRoster) {
        this.homeRoster = homeRoster;
    }

    public void setAwayRoster(List<String> awayRoster) {
        this.awayRoster = awayRoster;
    }

    public MatchInfo(LocalDate matchDate, LocalTime matchTime, Long homeTeamId, Long awayTeamId, String liveLink) {
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.liveLink = liveLink;
    }
}
