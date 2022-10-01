package com.lofserver.soma.service.api.match;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.repository.LeagueRepository;
import com.lofserver.soma.service.api.user.UserRepositoryService;
import com.lofserver.soma.service.view.CommunityViewService;
import com.lofserver.soma.service.view.MatchViewService;
import com.lofserver.soma.service.view.VideoViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> getMatchListByMonth(Long userId, Boolean isAll, LocalDate date){

        List<CommonItem> commonItemList = new ArrayList<>();
        LocalDate dateInfo = LocalDate.of(date.getYear(), date.getMonthValue(), 1);
        while(true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", dateInfo.getMonthValue() + "월" + dateInfo.getDayOfMonth() + "일");
            commonItemList.add(new CommonItem(jsonObject));
            if(dateInfo.getMonthValue() != date.getMonthValue())
                break;
            commonItemList.addAll(matchViewService.getMatchListByDate(userId, dateInfo, isAll));
            dateInfo = dateInfo.plusDays(1);
        }

        return new ResponseEntity<>(commonItemList, HttpStatus.OK);
    }

}
