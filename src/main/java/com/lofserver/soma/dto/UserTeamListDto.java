package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserTeamListDto {
    private Long userId;
    private List<Long> teamIdList;
}
