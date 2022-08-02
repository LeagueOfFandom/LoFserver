package com.lofserver.soma.entity.match;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class MatchInfo {
    private LocalDate matchDate;
    private LocalTime matchTime;
    private Long homeTeamId;
    private Long awayTeamId;
    private String liveLink;
    private Map<String, String> roster = new HashMap<>();
    public void setRoster(String role, String name) {
        roster.put(role, name);
    }

    public MatchInfo(LocalDate matchDate, LocalTime matchTime, Long homeTeamId, Long awayTeamId, String liveLink) {
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.liveLink = liveLink;
    }
}
