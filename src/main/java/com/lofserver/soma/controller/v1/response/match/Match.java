package com.lofserver.soma.controller.v1.response.match;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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
    @ApiModelProperty(example = "0")
    private Long homeScore;
    @ApiModelProperty(example = "https://")
    private String homeImg;
    @ApiModelProperty(example = "DRX")
    private String awayName;
    @ApiModelProperty(example = "2")
    private Long awayScore;
    @ApiModelProperty(example = "https://")
    private String awayImg;
    @ApiModelProperty(example = "true")
    private Boolean live;
    @ApiModelProperty(example = "https://")
    private String liveLink;
    @ApiModelProperty(example = "true")
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
