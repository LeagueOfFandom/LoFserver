package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.TeamVsTeamSetInfo;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchDetails;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.dto.crawlDto.gameDto.sub.teams.Team;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.Game;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.Result;
import com.lofserver.soma.entity.MatchDetailEntity;
import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.repository.MatchDetailRepository;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Slf4j
@RequiredArgsConstructor
public class LofService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    private final MatchDetailRepository matchDetailRepository;

    public ResponseEntity<?> getTeamVsTeam(Long matchId){
        MatchEntity matchEntity = matchRepository.findById(matchId).orElse(null);
        if(matchEntity == null)
            return new ResponseEntity<>("해당 id 없음",HttpStatus.NOT_FOUND);

        List<MatchDetailEntity> matchDetailEntityList = new ArrayList<>();
        for(Game game : matchEntity.getGames()){
            MatchDetailEntity matchDetailEntity = matchDetailRepository.findById(game.getId()).orElse(null);
            matchDetailEntityList.add(matchDetailEntity);
        }
        return new ResponseEntity<>(matchDetailEntityList, HttpStatus.OK);
    }

    //user의 matchList반환 함수.
    public ResponseEntity<?> getMatchList(Long userId, Boolean isAll, Boolean isAfter, int page){
        final int PAGE_PER_LOCALDATE = 5;
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }

        Map<LocalDate, List<MatchDetails>> matchList = new HashMap<>();
        List<MatchDetails> liveList = new ArrayList<>();

        List<Long> teamList = userEntity.getTeamList();
        log.info(teamList.toString());
        TreeSet<Long> matchListID = new TreeSet<>();
        Map<Long, Boolean> userSelected = userEntity.getUserSelected();

        //선택한 팀이 없거나 전부 보여달라고 하면 모든 경기 추가.
        if(teamList.isEmpty() || isAll)
            matchListID.addAll(matchRepository.findAllId());
        else
            teamList.forEach(teamId -> {
                matchListID.addAll(matchRepository.findAllByTeamId(teamId));
            });

        Set<Long> matchListByTeam = new HashSet<>();
        teamList.forEach(teamId -> {
            matchListByTeam.addAll(matchRepository.findAllByTeamId(teamId));
        });

        matchListID.forEach(matchId -> {
            MatchEntity matchEntity =  matchRepository.findById(matchId).orElse(null);
            //오늘 날짜를 기준으로 정렬.(이전경기 or 이후경기)
            if(matchEntity.getOriginal_scheduled_at() == null) return;
            if(isAfter && matchEntity.getOriginal_scheduled_at().isBefore(LocalDateTime.now(ZoneId.of("Asia/Seoul")))) return;
            if(!isAfter && matchEntity.getOriginal_scheduled_at().isAfter(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1))) return;
            //설정한 값이 있다면 설정한 값으로 진행한다.
            if(userSelected.containsKey(matchId)) {
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(userSelected.get(matchId)));
                else {
                    if (matchList.containsKey(matchEntity.getOriginal_scheduled_at().toLocalDate()))
                        matchList.get(matchEntity.getOriginal_scheduled_at().toLocalDate()).add(matchEntity.toMatchDetails(userSelected.get(matchId)));
                    else {
                        List<MatchDetails> matchDetailsList = new ArrayList<>();
                        matchDetailsList.add(matchEntity.toMatchDetails(userSelected.get(matchId)));
                        matchList.put(matchEntity.getOriginal_scheduled_at().toLocalDate(), matchDetailsList);
                    }
                }
            }
            //선택한 팀이라면 알람을 보내준다.
            else if(matchListByTeam.contains(matchId)) {
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(true));
                else {
                    if (matchList.containsKey(matchEntity.getOriginal_scheduled_at().toLocalDate()))
                        matchList.get(matchEntity.getOriginal_scheduled_at().toLocalDate()).add(matchEntity.toMatchDetails(true));
                    else {
                        List<MatchDetails> matchDetailsList = new ArrayList<>();
                        matchDetailsList.add(matchEntity.toMatchDetails(true));
                        matchList.put(matchEntity.getOriginal_scheduled_at().toLocalDate(), matchDetailsList);
                    }
                }
            }
            //아니라면 안보낸다.
            else {
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(false));
                else {
                    if (matchList.containsKey(matchEntity.getOriginal_scheduled_at().toLocalDate()))
                        matchList.get(matchEntity.getOriginal_scheduled_at().toLocalDate()).add(matchEntity.toMatchDetails(false));
                    else {
                        List<MatchDetails> matchDetailsList = new ArrayList<>();
                        matchDetailsList.add(matchEntity.toMatchDetails(false));
                        matchList.put(matchEntity.getOriginal_scheduled_at().toLocalDate(), matchDetailsList);
                    }
                }
            }
        });

        Long totalPage = null;
        List<Match> returnMatchList = new ArrayList<>();
        if(isAfter) {
            TreeSet<LocalDate> dateList = new TreeSet<>();
            dateList.addAll(matchList.keySet());
            AtomicInteger pagecheck = new AtomicInteger(0);
            totalPage = (long) Math.ceil((double) dateList.size()/PAGE_PER_LOCALDATE) - 1;
            dateList.forEach(localDate -> {
                if(pagecheck.get() >= page * PAGE_PER_LOCALDATE && pagecheck.get() < (page + 1)* PAGE_PER_LOCALDATE)
                    returnMatchList.add(new Match(localDate, matchList.get(localDate)));
                pagecheck.getAndIncrement();
            });
        }
        else{
            NavigableSet<LocalDate> dateList = new TreeSet<>();
            dateList.addAll(matchList.keySet());
            dateList = dateList.descendingSet();
            AtomicInteger pagecheck = new AtomicInteger(0);
            totalPage = (long) Math.ceil((double) dateList.size()/PAGE_PER_LOCALDATE) - 1;
            dateList.forEach(localDate -> {
                if(pagecheck.get() >= page * PAGE_PER_LOCALDATE && pagecheck.get() < (page + 1)* PAGE_PER_LOCALDATE)
                    returnMatchList.add(new Match(localDate, matchList.get(localDate)));
                pagecheck.getAndIncrement();
            });
        }

        return new ResponseEntity<>(new MatchList(new Match(LocalDate.now(ZoneId.of("Asia/Seoul")),liveList) ,returnMatchList,totalPage,new Long(page)), HttpStatus.OK);
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
            if(userEntity.getTeamList().contains(teamEntity.getId())) userTeamInfoList.add(new UserTeamInfo(teamEntity,true));
            else userTeamInfoList.add(new UserTeamInfo(teamEntity, false));
        });
        return new ResponseEntity<>(new UserTeamInfoList(userTeamInfoList),HttpStatus.OK );
    }
    //초기 user set 함수.
    public ResponseEntity<UserId> setUser(UserDto userDto){
        UserEntity userEntity = userRepository.findByDeviceId(userDto.getDeviceId());
        //없다면 저장한다.
        if(userEntity == null)
            return new ResponseEntity<>(new UserId(userRepository.save(new UserEntity(userDto.getToken(), userDto.getDeviceId())).getUserId(),false), HttpStatus.OK);
        //있다면 반환한다.
        return new ResponseEntity<>(new UserId(userEntity.getUserId(), true), HttpStatus.OK);
    }
    //user의 팀 정보 수정.
    public ResponseEntity<String> setTeamList(UserTeamListDto userTeamListDto){
        UserEntity userEntity = userRepository.findById(userTeamListDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList: "+"해당 id({}) 없음" , userTeamListDto.getUserId());
            return new ResponseEntity<>("해당 user id 없음", HttpStatus.BAD_REQUEST);
        }
        if(!teamRepository.findAllId().containsAll(userTeamListDto.getTeamIdList())){
            log.info("getMatchList: "+"팀 id({}) 리스트 잘못됨" , userTeamListDto.getTeamIdList().toString());
            return new ResponseEntity<>("해당 team id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.setTeamList(userTeamListDto.getTeamIdList());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    public ResponseEntity<String> setAlarm(UserAlarmDto userAlarmDto){
        UserEntity userEntity = userRepository.findById(userAlarmDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("setAlarm: " + "해당 id({}) 없음" , userAlarmDto.getUserId());
            return new ResponseEntity<>("해당 id 없음", HttpStatus.BAD_REQUEST);
        }
        if(matchRepository.findById(userAlarmDto.getMatchId()).orElse(null) == null){
            log.info("setAlarm: " + "해당 match id({}) 없음" + userAlarmDto.getMatchId());
            return new ResponseEntity<>("해당 match id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.addUserSelected(userAlarmDto.getMatchId(),userAlarmDto.getAlarm());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
