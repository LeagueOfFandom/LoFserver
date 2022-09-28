package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.match.sub.MatchViewObject;
import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchViewService {

    private final MatchRepository matchRepository;
    private final UserService userService;

    public List<CommonItem> getMatchListTest(){
        return getMatchListByDate(1L, LocalDate.parse("2022-09-29"), false);
    }

    /**
     * 유저에 맞는 매치 리스트 조회 ( 전체 / 유저선택 팀만 )
     * @param userId
     * @param date
     * @param onlyUserTeam
     * @return List<CommonItem>
     */
    public List<CommonItem> getMatchListByDate(Long userId, LocalDate date, Boolean onlyUserTeam) {
        List<CommonItem> commonItemList = new ArrayList<>();

        List<MatchEntity> matchEntityList = matchRepository.findAllByLeagueIdInAndOriginalScheduledAtBetween(
                userService.getUserLeagueList(userId),
                date.atStartOfDay(),
                date.atTime(23, 59, 59));

        matchEntityList.forEach(matchEntity -> {
            //비어있는 경기 제외
            if (matchEntity.getOpponents() == null || matchEntity.getOpponents().size() == 0)
                return;
            //유저가 선택한 경기만 조회
            if (onlyUserTeam && !userService.checkUserTeamByMatchEntity(userId, matchEntity))
                return;

            Boolean isAlarm = userService.checkUserMatchSetAlarmByMatchEntity(userId, matchEntity);

            CommonItem commonItem = new CommonItem(new MatchViewObject(matchEntity, isAlarm));
            commonItemList.add(commonItem);
        });

        return commonItemList;
    }

    public List<CommonItem> getLiveMatch(Long userId){
        List<CommonItem> commonItemList = new ArrayList<>();
        List<MatchEntity> matchEntityList = matchRepository.findAllByStatus("running");

        matchEntityList.forEach(matchEntity -> {
            Boolean isAlarm = userService.checkUserMatchSetAlarmByMatchEntity(userId, matchEntity);
            CommonItem commonItem = new CommonItem(new MatchViewObject(matchEntity, isAlarm));
            commonItemList.add(commonItem);
        });

        return commonItemList;
    }
}
