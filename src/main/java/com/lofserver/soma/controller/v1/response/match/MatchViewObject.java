package com.lofserver.soma.controller.v1.response.match;

import com.lofserver.soma.dto.pandaScoreDto.matchDto.sub.Opponent;
import com.lofserver.soma.entity.MatchEntity;
import lombok.Getter;

@Getter
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

    private String status;

    public MatchViewObject(MatchEntity matchEntity, Boolean isAlarm){
        this.matchId = matchEntity.getId();
        this.homeName = matchEntity.getOpponents().get(0).getOpponent().getAcronym();
        this.homeImg = matchEntity.getOpponents().get(0).getOpponent().getImage_url();
        this.awayName = matchEntity.getOpponents().get(1).getOpponent().getAcronym();
        this.awayImg = matchEntity.getOpponents().get(1).getOpponent().getImage_url();
        this.time = matchEntity.getBeginAt().toString();
        this.league = matchEntity.getTournament().getName();
        this.isAlarm = isAlarm;
        this.homeScore = matchEntity.getResults().get(0).getScore();
        this.awayScore = matchEntity.getResults().get(1).getScore();
        this.status = matchEntity.getStatus();
    }
}
