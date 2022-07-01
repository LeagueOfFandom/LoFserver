package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAlarmDto {
    private Long userId;
    private Long matchId;
    private Boolean alarm;

    public UserAlarmDto(Long userId, Long matchId, Boolean alarm) {
        this.userId = userId;
        this.matchId = matchId;
        this.alarm = alarm;
    }
}
