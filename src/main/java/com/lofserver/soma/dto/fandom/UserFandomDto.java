package com.lofserver.soma.dto.fandom;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserFandomDto {
    private Long userId;
    private List<TeamId> teamIdList;
}
