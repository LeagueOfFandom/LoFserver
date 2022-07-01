package com.lofserver.soma.controller.v1.response.team;

import lombok.Getter;

import java.util.List;

@Getter
public class UserTeamInfoList {
    private List<UserTeamInfo> userTeamInfoList;
    public UserTeamInfoList(List<UserTeamInfo> userTeamInfoList) {
        this.userTeamInfoList = userTeamInfoList;
    }
}
