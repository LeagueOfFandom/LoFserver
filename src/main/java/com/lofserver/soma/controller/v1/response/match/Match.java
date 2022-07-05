package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
public class Match {
    private Long matchId;
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String homeName;
    private Long homeScore;
    private String homeImg;
    private String awayName;
    private Long awayScore;
    private String awayImg;
    private Boolean live;
    private String liveLink;
    private Boolean alarm;

    public Match(Long matchId, LocalDate matchDate, LocalTime matchTime, String homeName, Long homeScore, String homeImg, String awayName, Long awayScore, String awayImg, Boolean live, String liveLink, Boolean alarm) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeName = homeName;
        this.homeScore = homeScore;
        this.homeImg = homeImg;
        this.awayName = awayName;
        this.awayScore = awayScore;
        this.awayImg = awayImg;
        this.live = live;
        this.liveLink = liveLink;
        this.alarm = alarm;
    }
}
