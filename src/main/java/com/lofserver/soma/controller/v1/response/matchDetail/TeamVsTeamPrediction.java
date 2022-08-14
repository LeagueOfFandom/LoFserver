package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

@Getter
public class TeamVsTeamPrediction {
    private Long blueTeamWin;
    private Long redTeamWin;

    public TeamVsTeamPrediction(Long blueTeamWin, Long redTeamWin) {
        this.blueTeamWin = blueTeamWin;
        this.redTeamWin = redTeamWin;
    }
}
