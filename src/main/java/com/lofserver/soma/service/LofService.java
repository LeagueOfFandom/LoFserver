package com.lofserver.soma.service;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.sub.DateInfo;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.match.sub.MatchViewObject;
import com.lofserver.soma.controller.v1.response.team.LeagueInfo;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.data.ViewType;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.google.GoogleUserInfoDto;
import com.lofserver.soma.dto.pandaScoreDto.matchDto.sub.Opponent;
import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class LofService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final JsonWebToken jsonWebToken;
    private final MatchViewService matchViewService;
    private final VideoViewService videoViewService;
    private final CommunityViewService communityViewService;

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

    public ResponseEntity<?> setFcm(String fcmToken, Long id){
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if(userEntity == null){
            return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
        }
        userEntity.setToken(fcmToken);
        userRepository.save(userEntity);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    //user의 matchList반환 함수.
    public ResponseEntity<?> getMatchList(Long userId, Boolean isAll, LocalDate date){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null)
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);

        List<Long> leagueList = new ArrayList<>();
        leagueList.add(297L); //worlds
        List<DateInfo> dateInfoList = new ArrayList<>();
        List<List<CommonItem>> commonItemListList = new ArrayList<>();

        for(int i = 0; i < 7; i++) {
            //dateInfo setting
            dateInfoList.add(new DateInfo(date.plusDays(i)));

            //localDate -> LocalDateTime
            LocalDateTime start = date.plusDays(i).atStartOfDay();
            LocalDateTime end = date.plusDays(i).atTime(23, 59, 59);

            //get matchList
            List<MatchEntity> matchEntityList = matchRepository.findAllByLeagueIdInAndOriginalScheduledAtBetween(leagueList, start, end);

            //commonItemList setting
            List<CommonItem> commonItemList = new ArrayList<>();
            for(MatchEntity matchEntity : matchEntityList){

                if(matchEntity.getOpponents() == null || matchEntity.getOpponents().size() == 0)
                    continue;

                Opponent homeTeam = matchEntity.getOpponents().get(0).getOpponent();
                Opponent awayTeam = matchEntity.getOpponents().get(1).getOpponent();

                if(!isAll && !userEntity.getTeamList().contains(homeTeam.getId()) && !userEntity.getTeamList().contains(awayTeam.getId()))
                    continue;

                Boolean isAlarm = false;
                if(userEntity.getUserSelected().containsKey(matchEntity.getId()))
                    isAlarm = userEntity.getUserSelected().get(matchEntity.getId());
                else if (userEntity.getTeamList().contains(homeTeam.getId()) || userEntity.getTeamList().contains(awayTeam.getId()))
                    isAlarm = true;


                CommonItem commonItem = new CommonItem(new MatchViewObject(matchEntity,isAlarm));
                commonItemList.add(commonItem);
            }
            commonItemListList.add(commonItemList);
        }
        return new ResponseEntity<>(new MatchList(dateInfoList,commonItemListList), HttpStatus.OK);
    }

    public ResponseEntity<?> getTeamListByUser(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getTeamListByUser" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }
        List<Long> selectedTeamList = userEntity.getTeamList();
        List<TeamEntity> teamEntityList = teamRepository.findAllById(selectedTeamList);
        List<TeamInfo> teamInfoList = new ArrayList<>();
        teamEntityList.forEach(teamEntity -> {
            TeamInfo teamInfo = new TeamInfo(teamEntity,true, leagueRepository.findBySeriesId(teamEntity.getSeriesId()).getName());
            teamInfoList.add(teamInfo);
        });

        return new ResponseEntity<>(teamInfoList, HttpStatus.OK);
    }
    //user가 선택한 team list 제공 함수.
    public ResponseEntity<?> getTeamList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getTeamList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }
        List<String> leagues = new ArrayList<>();
        leagues.add("LCK");
        leagues.add("LPL");
        leagues.add("LEC");
        leagues.add("LCS");

        List<LeagueInfo> leagueInfos = new ArrayList<>();
        leagues.forEach(league -> {
            log.info(league);
            Long seriesId = leagueRepository.findByName(league).getLatest_series_id();
            List<TeamEntity> teamEntityList = teamRepository.findAllBySeriesId(seriesId);
            LeagueInfo leagueInfo = new LeagueInfo("(have to fix)this is " + league);
            List<TeamInfo> teamInfos = new ArrayList<>();

            teamEntityList.forEach(teamEntity -> {
                if(userEntity.getTeamList().contains(teamEntity.getId()))
                    teamInfos.add(new TeamInfo(teamEntity, true, league));
                else
                    teamInfos.add(new TeamInfo(teamEntity, false, league));
            });
            leagueInfo.setTeamInfo(teamInfos);
            leagueInfos.add(leagueInfo);
        });

        return new ResponseEntity<>(new LeagueList(leagueInfos,leagues), HttpStatus.OK);
    }
    //초기 user set 함수.
    public ResponseEntity<?> setUser(UserDto userDto){

        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + userDto.getGoogleAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        GoogleUserInfoDto googleUserInfo;

        try {
            ResponseEntity<GoogleUserInfoDto> googleUserInfoDto = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserInfoDto.class);
            googleUserInfo = googleUserInfoDto.getBody();
        }catch (Exception e){
            log.info("googleLogin : {}",e);
            return new ResponseEntity<>("googleAccessToken 오류",HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = userRepository.findByEmail(googleUserInfo.getEmail());

        //없다면 저장한다.
        if(userEntity == null) {
            userEntity = new UserEntity(userDto.getFcmToken(),googleUserInfo.getEmail(), googleUserInfo.getName(), googleUserInfo.getPicture());
            String jwtToken = jsonWebToken.makeJwtTokenById(userRepository.save(userEntity).getUserId());
            return new ResponseEntity<>(new UserId(jwtToken , false), HttpStatus.OK);
        }
        //있다면 반환한다.
        String jwtToken = jsonWebToken.makeJwtTokenById(userEntity.getUserId());
        return new ResponseEntity<>(new UserId(jwtToken, true), HttpStatus.OK);
    }
    //user의 팀 정보 수정.
    public ResponseEntity<String> setTeamList(List<Long> userTeamListDto, Long id){
        UserEntity userEntity = userRepository.findById(id).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList: "+"해당 id({}) 없음" , id);
            return new ResponseEntity<>("해당 user id 없음", HttpStatus.BAD_REQUEST);
        }
        if(!teamRepository.findAllId().containsAll(userTeamListDto)){
            log.info("getMatchList: "+"팀 id({}) 리스트 잘못됨" , userTeamListDto.toString());
            return new ResponseEntity<>("해당 team id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.setTeamList(userTeamListDto);
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
