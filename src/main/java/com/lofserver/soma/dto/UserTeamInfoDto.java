package com.lofserver.soma.dto;

import com.lofserver.soma.domain.TeamEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
public class UserTeamInfoDto {
    private Long teamId;
    private String teamName;
    private String teamImg;
    private boolean teamCheck;

    public UserTeamInfoDto(TeamEntity teamEntity, boolean teamCheck) {

        this.teamId = teamEntity.getTeamId();
        this.teamName = teamEntity.getTeamName();
        this.teamImg = teamEntity.getTeamImg();
        this.teamCheck = teamCheck;
    }
}
