package com.lofserver.soma.service.api.match;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.match.sub.DateInfo;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.repository.*;
import com.lofserver.soma.service.api.user.UserRepositoryService;
import com.lofserver.soma.service.view.CommunityViewService;
import com.lofserver.soma.service.view.MatchViewService;
import com.lofserver.soma.service.view.VideoViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class MatchApiService {
    private final LeagueRepository leagueRepository;
    private final JsonWebToken jsonWebToken;
    private final MatchViewService matchViewService;
    private final VideoViewService videoViewService;
    private final CommunityViewService communityViewService;

    private final UserRepositoryService userRepositoryService;

    /** main test page */
    private List<CommonItem> getMainPageTest(){
        List<CommonItem> commonItems = new ArrayList<>();
        commonItems.addAll(matchViewService.getLiveMatch(1L));
        commonItems.addAll(matchViewService.getMatchListTest());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", "실시간 인기글");
        commonItems.add(new CommonItem(jsonObject));
        commonItems.add(communityViewService.getCommunityListTest());
        commonItems.add(communityViewService.getCommunityListTest());
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("text", "하이라이트");
        commonItems.add(new CommonItem(jsonObject2));
        commonItems.add(videoViewService.getVideoListTest());
        return commonItems;
    }

    public ResponseEntity<?> getMainPage(Long id){
        if (id == 1L)
            return new ResponseEntity<>(getMainPageTest(), HttpStatus.OK);

        List<CommonItem> commonItemList = new ArrayList<>();
        //live 추가
        commonItemList.addAll(matchViewService.getLiveMatch(id));
        //오늘 경기 추가
        commonItemList.addAll(matchViewService.getMatchListByDate(id, LocalDate.now(), false));

        return new ResponseEntity<>(commonItemList, HttpStatus.OK);
    }

    public ResponseEntity<?> getMatchList(Long userId, Boolean isAll, LocalDate date){

        List<DateInfo> dateInfoList = new ArrayList<>();
        List<List<CommonItem>> commonItemListList = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            dateInfoList.add(new DateInfo(date.plusDays(i)));
            commonItemListList.add(matchViewService.getMatchListByDate(userId, date.plusDays(i), isAll));
        }
        return new ResponseEntity<>(new MatchList(dateInfoList,commonItemListList), HttpStatus.OK);
    }

}
