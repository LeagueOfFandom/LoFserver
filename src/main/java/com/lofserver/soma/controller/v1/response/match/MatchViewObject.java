package com.lofserver.soma.controller.v1.response.match;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchViewObject {
    private Long matchId;
    private String homeName;
    private String homeImg;
    private String awayName;
    private String awayImg;
    private String time;
    private String league;
    private Boolean isAlarm;
    private Long homeScore;
    private Long awayScore;
}
