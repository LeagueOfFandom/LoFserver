package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTeamlistDto {
    private Long userTeamlistId;
    private Long userId;
    private Long teamId;

    public UserTeamlistDto(Long userTeamlistId, Long userId, Long teamId) {
        this.userTeamlistId = userTeamlistId;
        this.userId = userId;
        this.teamId = teamId;
    }
}
