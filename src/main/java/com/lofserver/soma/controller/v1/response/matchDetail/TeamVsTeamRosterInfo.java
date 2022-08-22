package com.lofserver.soma.controller.v1.response.matchDetail;

import com.lofserver.soma.controller.v1.response.Roster;
import com.lofserver.soma.dto.crawlDto.gameDto.sub.teams.Team;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamVsTeamRosterInfo {
    private List<Roster> blueTeam;
    private List<Roster> redTeam;

    public TeamVsTeamRosterInfo(List<Roster> blueTeam, List<Roster> redTeam) {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
    }


}
