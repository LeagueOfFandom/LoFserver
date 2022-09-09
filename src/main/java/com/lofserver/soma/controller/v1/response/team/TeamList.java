package com.lofserver.soma.controller.v1.response.team;

import com.lofserver.soma.entity.TeamEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
public class TeamList {
    @ApiModelProperty(example = "1")
    private Long teamId;
    @ApiModelProperty(example = "T1")
    private String teamName;
    @ApiModelProperty(example = "https://")
    private String teamImg;
    @ApiModelProperty(example = "true")
    private boolean teamCheck;

    @ApiModelProperty(example = "lck")
    private String league;

    public TeamList(TeamEntity teamEntity, Boolean teamCheck ,String league) {
        this.teamId = teamEntity.getId();
        this.teamName = teamEntity.getAcronym();
        this.teamImg = teamEntity.getImage_url();
        this.teamCheck = teamCheck;
        this.league = league;
    }
}
