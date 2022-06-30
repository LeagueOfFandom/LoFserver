package com.lofserver.soma.dto;


import com.lofserver.soma.domain.TeamEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
public class TeamDto {
    private Long teamId;
    private Long leagueId;
    private String teamName;
    private String teamImg;

    public TeamDto(Long teamId, Long leagueId, String teamName, String teamImg) {
        this.teamId = teamId;
        this.leagueId = leagueId;
        this.teamName = teamName;
        this.teamImg = teamImg;
    }

    public TeamEntity toEntity(){
        return new TeamEntity(null, this.leagueId, this.teamName, this.teamImg);
    }
}
