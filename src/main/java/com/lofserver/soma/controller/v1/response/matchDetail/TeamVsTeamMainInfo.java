package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

@Getter
public class TeamVsTeamMainInfo {
    private String blueTeamAcronym;
    private String redTeamAcronym;
    private Long blueTeamId;
    private Long redTeamId;
    private String blueTeamImageUrl;
    private String redTeamImageUrl;
    private Long blueTeamScrore;
    private Long redTeamScrore;

    public TeamVsTeamMainInfo(String blueTeamAcronym, String redTeamAcronym, Long blueTeamId, Long redTeamId, String blueTeamImageUrl, String redTeamImageUrl, Long blueTeamScrore, Long redTeamScrore) {
        this.blueTeamAcronym = blueTeamAcronym;
        this.redTeamAcronym = redTeamAcronym;
        this.blueTeamId = blueTeamId;
        this.redTeamId = redTeamId;
        this.blueTeamImageUrl = blueTeamImageUrl;
        this.redTeamImageUrl = redTeamImageUrl;
        this.blueTeamScrore = blueTeamScrore;
        this.redTeamScrore = redTeamScrore;
    }
}
