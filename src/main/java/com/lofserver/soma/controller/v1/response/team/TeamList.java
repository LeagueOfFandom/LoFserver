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

    public TeamList(Long teamId, String teamName, String teamImg, boolean teamCheck, String league) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamImg = teamImg;
        this.teamCheck = teamCheck;
        this.league = league;
    }
}
