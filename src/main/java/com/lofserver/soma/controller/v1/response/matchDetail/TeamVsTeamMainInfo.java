package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

@Getter
public class TeamVsTeamMainInfo {
    private String date;
    private String time;
    private String blueTeamAcronym;
    private String redTeamAcronym;
    private Long blueTeamId;
    private Long redTeamId;
    private String blueTeamImageUrl;
    private String redTeamImageUrl;
    private Long blueTeamScore;
    private Long redTeamScore;
    private Boolean blueWin;
    private Boolean redWin;

    public TeamVsTeamMainInfo(String date, String time, String blueTeamAcronym, String redTeamAcronym, Long blueTeamId, Long redTeamId, String blueTeamImageUrl, String redTeamImageUrl, Long blueTeamScore, Long redTeamScore, Boolean blueWin, Boolean redWin) {
        this.date = date;
        this.time = time;
        this.blueTeamAcronym = blueTeamAcronym;
        this.redTeamAcronym = redTeamAcronym;
        this.blueTeamId = blueTeamId;
        this.redTeamId = redTeamId;
        this.blueTeamImageUrl = blueTeamImageUrl;
        this.redTeamImageUrl = redTeamImageUrl;
        this.blueTeamScore = blueTeamScore;
        this.redTeamScore = redTeamScore;
        this.blueWin = blueWin;
        this.redWin = redWin;
    }
}
