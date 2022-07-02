package com.lofserver.soma.controller.v1.response.team;

import com.lofserver.soma.entity.TeamEntity;
import lombok.Getter;

@Getter
public class UserTeamInfo {
    private Long teamId;
    private String teamName;
    private String teamImg;
    private boolean teamCheck;

    public UserTeamInfo(TeamEntity teamEntity, boolean teamCheck) {

        this.teamId = teamEntity.getTeamId();
        this.teamName = teamEntity.getTeamName();
        this.teamImg = teamEntity.getTeamImg();
        this.teamCheck = teamCheck;
    }
}
