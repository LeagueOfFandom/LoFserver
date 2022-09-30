package com.lofserver.soma.service.team;

import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.service.league.LeagueRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamRepositoryService {
    private final TeamRepository teamRepository;
    private final LeagueRepositoryService leagueRepositoryService;

    public List<TeamInfo> getTeamInfoListByTeamIdList(List<Long> teamList) {
        List<TeamEntity> teamEntityList = teamRepository.findAllById(teamList);

        List<TeamInfo> teamInfoList = new ArrayList<>();
        teamEntityList.forEach(teamEntity -> {
            TeamInfo teamInfo = new TeamInfo(teamEntity,true, leagueRepositoryService.getLeagueNameByLeagueId(teamEntity.getLeagueId()));
            teamInfoList.add(teamInfo);
        });

        return teamInfoList;
    }

    public List<Long> getTeamListBySeriesId(Long seriesId) {
        return teamRepository.findAllIdBySeriesId(seriesId);
    }
    public List<Long> getSerieIdListByTeamIdList(List<Long> teamIdList) {
        return teamRepository.findSerieIdListByTeamIdList(teamIdList);
    }
}
