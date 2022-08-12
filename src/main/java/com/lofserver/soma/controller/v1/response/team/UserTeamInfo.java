package com.lofserver.soma.controller.v1.response.team;

import com.lofserver.soma.entity.TeamEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class UserTeamInfo {
    @ApiModelProperty(example = "1")
    private Long teamId;
    @ApiModelProperty(example = "담원 기아")
    private String teamName;
    @ApiModelProperty(example = "https://")
    private String teamImg;
    @ApiModelProperty(example = "true")
    private boolean teamCheck;

    public UserTeamInfo(TeamEntity teamEntity, boolean teamCheck) {
        this.teamId = teamEntity.getId();
        this.teamName = teamEntity.getName();
        this.teamImg = teamEntity.getImage_url();
        this.teamCheck = teamCheck;
    }
}
