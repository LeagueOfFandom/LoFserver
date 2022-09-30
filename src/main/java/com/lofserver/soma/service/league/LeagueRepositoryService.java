package com.lofserver.soma.service.league;

import com.lofserver.soma.controller.v1.response.team.LeagueInfo;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.entity.LeagueEntity;
import com.lofserver.soma.repository.LeagueRepository;
import com.lofserver.soma.service.team.TeamRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeagueRepositoryService {
    private final LeagueRepository leagueRepository;
    private final TeamRepositoryService teamRepositoryService;

    public String getLeagueNameByLeagueId(Long leagueId) {
        LeagueEntity leagueEntity = leagueRepository.findById(leagueId).orElse(null);
        if (leagueEntity == null)
            return null;

        return leagueEntity.getName();
    }

    public LeagueList getLeagueListByLeagueIdList(List<Long> leagueId) {
        List<LeagueEntity> leagueEntityList = leagueRepository.findAllById(leagueId);

        List<String> leagueNameList = new ArrayList<>();
        List<LeagueInfo> leagueInfoList = new ArrayList<>();
        leagueEntityList.forEach(leagueEntity -> {
            leagueNameList.add(leagueEntity.getName());

            List<Long> teamList = teamRepositoryService.getTeamListBySeriesId(leagueEntity.getLatestSeriesId());
            List<TeamInfo> teamInfoList = teamRepositoryService.getTeamInfoListByTeamIdList(teamList);

            LeagueInfo leagueInfo = LeagueInfo.builder()
                    .note(leagueEntity.getSlug())
                    .teamInfo(teamInfoList)
                    .build();

            leagueInfoList.add(leagueInfo);
        });

        return new LeagueList(leagueInfoList, leagueNameList);
    }

    public List<Long> getLeagueIdListByTeamIdList(List<Long> teamIdList) {
        List<Long> seriesIdList = teamRepositoryService.getSerieIdListByTeamIdList(teamIdList);
        return leagueRepository.findLeagueIdListBySerieIdList(seriesIdList);
    }
}
