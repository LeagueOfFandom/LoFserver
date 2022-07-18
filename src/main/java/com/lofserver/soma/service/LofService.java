package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.dto.fcm.FcmResponse;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class LofService {

    //필요한 Jpa 선언.
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private String language = "ko_KR";

    //user의 matchList반환 함수.
    public ResponseEntity<?> getMatchList(Long userId, Boolean isAll){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        List<Match> matchList = new ArrayList<>();

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }

        List<Long> teamList = userEntity.getTeamList();
        TreeSet<Long> matchListID = new TreeSet<>();
        Map<Long, Boolean> userSelected = userEntity.getUserSelected();

        //선택한 팀이 없으면 전부 보여준다.
        if(teamList.isEmpty()){
            matchListID.addAll(matchRepository.findAllId());
            matchListID.forEach(matchId -> {
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(userSelected.get(matchId), teamRepository,language));
                //선택한 팀이 없으므로 전부 false로 진행한다.
                else matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(false,teamRepository,language));
            });
        }
        //전부를 보내준다.
        else if(isAll){
            matchListID.addAll(matchRepository.findAllId());
            //설정한 팀에 따른 알람 제공.
            Set<Long> matchListByTeam = new HashSet<>();
            teamList.forEach(teamId -> {
                matchListByTeam.addAll(teamRepository.findById(teamId).orElse(null).getTeamMatchList());
            });

            matchListID.forEach(matchId -> {
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(userSelected.get(matchId),teamRepository,language));
                //선택한 팀이라면 알람을 보내준다.
                else if(matchListByTeam.contains(matchId)) matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(true,teamRepository,language));
                //아니라면 안보낸다.
                else matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(false,teamRepository,language));
            });
        }
        //해당 팀 정보만 보내준다.
        else{
            teamList.forEach(teamId -> {
                matchListID.addAll(teamRepository.findById(teamId).orElse(null).getTeamMatchList());
            });
            matchListID.forEach(matchId -> {
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) matchList.add(matchRepository.findById(matchId).orElse(null).toMatch(userSelected.get(matchId),teamRepository,language));
                else matchList.add(matchRepository.findById(matchId).orElse(null).toMatch( true,teamRepository,language));
            });
        }
        return new ResponseEntity<>(new MatchList(matchList), HttpStatus.OK);
    }
    //user가 선택한 team list 제공 함수.
    public ResponseEntity<?> getTeamList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getTeamList: "+"해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }

        List<UserTeamInfo> userTeamInfoList = new ArrayList<>();
        //모든 팀에 대해 user의 선택 값 적용하여 제공.
        teamRepository.findAll().forEach(teamEntity -> {
            if(userEntity.getTeamList().contains(teamEntity.getTeamId())) userTeamInfoList.add(new UserTeamInfo(teamEntity,true,language));
            else userTeamInfoList.add(new UserTeamInfo(teamEntity, false,language));
        });
        return new ResponseEntity<>(new UserTeamInfoList(userTeamInfoList),HttpStatus.OK );
    }
    //초기 user set 함수.
    public ResponseEntity<UserId> setUser(UserDto userDto){
        UserEntity userEntity = userRepository.findByDeviceId(userDto.getDeviceId());
        //없다면 저장한다.
        if(userEntity == null) return new ResponseEntity<>(new UserId(userRepository.save(new UserEntity(userDto.getToken(), userDto.getDeviceId())).getUserId(),false), HttpStatus.OK);
        //있다면 반환한다.
        return new ResponseEntity<>(new UserId(userEntity.getUserId(), true), HttpStatus.OK);
    }
    //user의 팀 정보 수정.
    public ResponseEntity<String> setTeamList(UserTeamListDto userTeamListDto){
        UserEntity userEntity = userRepository.findById(userTeamListDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList: "+"해당 id 없음 :" + userTeamListDto.getUserId());
            return new ResponseEntity<>("해당 user id 없음", HttpStatus.BAD_REQUEST);
        }
        if(!teamRepository.findAllId().containsAll(userTeamListDto.getTeamIdList())){
            log.info("getMatchList: "+"팀 id 리스트 잘못됨" + userTeamListDto.getTeamIdList().toString());
            return new ResponseEntity<>("해당 team id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.setTeamList(userTeamListDto.getTeamIdList());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    public ResponseEntity<String> setAlarm(UserAlarmDto userAlarmDto){
        UserEntity userEntity = userRepository.findById(userAlarmDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("setAlarm: " + "해당 id 없음 :" + userAlarmDto.getUserId());
            return new ResponseEntity<>("해당 id 없음", HttpStatus.BAD_REQUEST);
        }
        if(matchRepository.findById(userAlarmDto.getMatchId()).orElse(null) == null){
            log.info("setAlarm: " + "해당 match id 없음 :" + userAlarmDto.getMatchId());
            return new ResponseEntity<>("해당 match id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.addUserSelected(userAlarmDto.getMatchId(),userAlarmDto.getAlarm());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    public void post(){

        String url = "https://fcm.googleapis.com/fcm/send";
        String key = "AAAA2e5oM2A:APA91bFkeAZM_08Vbliwn3C5_IR2jWF1GPgAS_9YYp071tRNyFossJP23OOTFMjwFq7HQW4HMU7K5XKee32u3cx8ioAlylFxK7SruyNO1iJy3sacuir-29GdosKdlKCBl6B_YfZj0xjd";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "key=" + key);
        JSONObject jsonObject = new JSONObject();
        JSONObject dataJson = new JSONObject();
        dataJson.put("title", "test");
        dataJson.put("message","gogo");
        jsonObject.put("to","fDE7uf6PTySGj3x9IWMo4b:APA91bFPwq7U7mYF-9FeHSBQlz9RCsZIbPl0PGheEvkqm-lkpXkrt9kO3MOIqOlJpvfvVlr-t73G4TpyuS2Zb24G52kw6EMK3cidtdOdhQJhlJI-P3ev6woGoOo657pEh7TC3RjudTVU");
        jsonObject.put("data", dataJson);
        log.info(jsonObject.toJSONString());

        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toJSONString(),headers);

        ResponseEntity<FcmResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, FcmResponse.class);

    }
}
