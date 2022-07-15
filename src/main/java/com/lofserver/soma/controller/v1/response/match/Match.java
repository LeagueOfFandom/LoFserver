package com.lofserver.soma.controller.v1.response.match;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Match {
    @ApiModelProperty(example = "2")
    private Long matchId;
    @ApiModelProperty(example = "2022-06-15")
    private LocalDate matchDate;
    @ApiModelProperty(example = "20:00:00")
    private LocalTime matchTime;
    @ApiModelProperty(example = "농심 레드포스")
    private String homeName;
    @ApiModelProperty(example = "DRX")
    private String awayName;
    @ApiModelProperty(example = "https://")
    private String homeImg;
    @ApiModelProperty(example = "https://")
    private String awayImg;
    @ApiModelProperty(example = "0")
    private Long homeScore;
    @ApiModelProperty(example = "2")
    private Long awayScore;
    @ApiModelProperty(example = "true")
    private Boolean live;
    @ApiModelProperty(example = "https://")
    private String liveLink;
    @ApiModelProperty(example = "true")
    private Boolean alarm;

    public Match(Long matchId, LocalDate matchDate, LocalTime matchTime, String homeName, String awayName, String homeImg, String awayImg, Long homeScore, Long awayScore, Boolean live, String liveLink, Boolean alarm) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.homeName = homeName;
        this.awayName = awayName;
        this.homeImg = homeImg;
        this.awayImg = awayImg;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.live = live;
        this.liveLink = liveLink;
        this.alarm = alarm;
    }
}
