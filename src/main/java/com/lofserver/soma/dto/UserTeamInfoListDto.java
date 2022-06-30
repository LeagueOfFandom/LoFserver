package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class UserTeamInfoListDto {
    private List<UserTeamInfoDto> userTeamInfoDtoList;

    public UserTeamInfoListDto(List<UserTeamInfoDto> userTeamInfoDtoList) {
        this.userTeamInfoDtoList = userTeamInfoDtoList;
    }

    public void addTeamInfo(UserTeamInfoDto userTeamInfoDto) {

        this.userTeamInfoDtoList.add(userTeamInfoDto);

    }
}
