package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.Roster;
import com.lofserver.soma.controller.v1.response.TeamVsTeam;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchDetails;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class LofService {

    //필요한 Jpa 선언.
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    //user의 matchList반환 함수.
    public ResponseEntity<?> getAfterMatchList(Long userId, Boolean isAll, Boolean isAfter){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }

        Map<LocalDate, List<MatchDetails>> matchList = new HashMap<>();
        List<MatchDetails> liveList = new ArrayList<>();

        List<Long> teamList = userEntity.getTeamList();
        TreeSet<Long> matchListID = new TreeSet<>();
        Map<Long, Boolean> userSelected = userEntity.getUserSelected();

        //선택한 팀이 없으면 전부 보여준다.
        if(teamList.isEmpty()){
            matchListID.addAll(matchRepository.findAllId());
            matchListID.forEach(matchId -> {
                MatchEntity matchEntity =  matchRepository.findById(matchId).orElse(null);
                if(isAfter && matchEntity.getMatchInfo().getMatchDate().isBefore(LocalDate.now(ZoneId.of("Asia/Seoul")))) return;
                if(!isAfter && matchEntity.getMatchInfo().getMatchDate().isAfter(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1))) return;
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                    else{
                        if(matchList.containsKey(matchEntity.getMatchInfo().getMatchDate())) {
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                        }
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
                //선택한 팀이 없으므로 전부 false로 진행한다.
                else {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(false, teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(false, teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(false, teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
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
                MatchEntity matchEntity =  matchRepository.findById(matchId).orElse(null);
                if(isAfter && matchEntity.getMatchInfo().getMatchDate().isBefore(LocalDate.now(ZoneId.of("Asia/Seoul")))) return;
                if(!isAfter && matchEntity.getMatchInfo().getMatchDate().isAfter(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1))) return;
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
                //선택한 팀이라면 알람을 보내준다.
                else if(matchListByTeam.contains(matchId)) {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(true, teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(true, teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(true, teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
                //아니라면 안보낸다.
                else {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(false, teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(false, teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(false, teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
            });
        }
        //해당 팀 정보만 보내준다.
        else{
            teamList.forEach(teamId -> {
                matchListID.addAll(teamRepository.findById(teamId).orElse(null).getTeamMatchList());
            });
            matchListID.forEach(matchId -> {
                MatchEntity matchEntity =  matchRepository.findById(matchId).orElse(null);
                if(isAfter && matchEntity.getMatchInfo().getMatchDate().isBefore(LocalDate.now(ZoneId.of("Asia/Seoul")))) return;
                if(!isAfter && matchEntity.getMatchInfo().getMatchDate().isAfter(LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1))) return;
                //설정한 값이 있다면 설정한 값으로 진행한다.
                if(userSelected.containsKey(matchId)) {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(userSelected.get(matchId), teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
                else {
                    if(matchEntity.getLive() == true) liveList.add(matchEntity.toMatchDetails(true, teamRepository));
                    else {
                        if (matchList.containsKey(matchEntity.getMatchInfo().getMatchDate()))
                            matchList.get(matchEntity.getMatchInfo().getMatchDate()).add(matchEntity.toMatchDetails(true, teamRepository));
                        else {
                            List<MatchDetails> matchDetailsList = new ArrayList<>();
                            matchDetailsList.add(matchEntity.toMatchDetails(true, teamRepository));
                            matchList.put(matchEntity.getMatchInfo().getMatchDate(), matchDetailsList);
                        }
                    }
                }
            });
        }

        List<Match> returnMatchList = new ArrayList<>();
        if(isAfter) {
            TreeSet<LocalDate> dateList = new TreeSet<>();
            dateList.addAll(matchList.keySet());
            dateList.forEach(localDate -> {
                returnMatchList.add(new Match(localDate, matchList.get(localDate)));
            });
        }
        else{
            NavigableSet<LocalDate> dateList = new TreeSet<>();
            dateList.addAll(matchList.keySet());
            dateList = dateList.descendingSet();
            dateList.forEach(localDate -> {
                returnMatchList.add(new Match(localDate, matchList.get(localDate)));
            });
        }

        return new ResponseEntity<>(new MatchList(new Match(LocalDate.now(ZoneId.of("Asia/Seoul")),liveList), returnMatchList), HttpStatus.OK);
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
            if(userEntity.getTeamList().contains(teamEntity.getTeamId())) userTeamInfoList.add(new UserTeamInfo(teamEntity,true));
            else userTeamInfoList.add(new UserTeamInfo(teamEntity, false));
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

    public ResponseEntity<?> getTeamVsTeam(Long matchId){
        MatchEntity nowMatch = matchRepository.findById(matchId).orElse(null);
        if(nowMatch == null)
            return new ResponseEntity<>("해당 match 없음",HttpStatus.BAD_REQUEST);
        Long homeTeamId = nowMatch.getMatchInfo().getHomeTeamId();
        Long awayTeamId = nowMatch.getMatchInfo().getAwayTeamId();

        Long homeTeamWinGame = 0L, awayTeamWinGame = 0L,homeTeamWinSet = 0L,awayTeamWinSet = 0L;
        List<MatchEntity> matchEntityList = matchRepository.findByHomeTeamIdAndAwayTeamId(homeTeamId,awayTeamId);
        for(MatchEntity matchEntity : matchEntityList){
            homeTeamWinSet += matchEntity.getHomeScore();
            awayTeamWinSet += matchEntity.getAwayScore();
            if(matchEntity.getHomeScore() > matchEntity.getAwayScore())
                homeTeamWinGame += 1;
            else if(matchEntity.getHomeScore() < matchEntity.getAwayScore())
                awayTeamWinGame += 1;
        }
        matchEntityList = matchRepository.findByHomeTeamIdAndAwayTeamId(awayTeamId,homeTeamId);
        for(MatchEntity matchEntity : matchEntityList){
            homeTeamWinSet += matchEntity.getAwayScore();
            awayTeamWinSet += matchEntity.getHomeScore();
            if(matchEntity.getAwayScore() > matchEntity.getHomeScore())
                homeTeamWinGame += 1;
            else if(matchEntity.getAwayScore() < matchEntity.getHomeScore())
                awayTeamWinGame += 1;
        }
        TeamEntity homeTeamEntity = teamRepository.findById(homeTeamId).orElse(null);
        TeamEntity awayTeamEntity = teamRepository.findById(awayTeamId).orElse(null);

        List<Roster> rosterList = new ArrayList<>();
        List<String> line = new ArrayList<>() {
            {
                add("TOP");
                add("JUNGLE");
                add("MID");
                add("BOTTOM");
                add("SUPPORT");
            }
        };
        line.forEach(lineName -> {
           rosterList.add(new Roster("Home"+lineName,homeTeamEntity.getRosterName().get(lineName),"https://d654rq93y7j8z.cloudfront.net/line/" + lineName +".png"));
           rosterList.add(new Roster("Away"+lineName,awayTeamEntity.getRosterName().get(lineName),"https://d654rq93y7j8z.cloudfront.net/line/" + lineName +".png"));
        });

        return new ResponseEntity<>(new TeamVsTeam(homeTeamEntity.getTeamName(),homeTeamWinGame,homeTeamWinSet,homeTeamEntity.getTeamImg(),awayTeamEntity.getTeamName(),awayTeamWinGame,awayTeamWinSet,awayTeamEntity.getTeamImg(), rosterList), HttpStatus.OK);
    }
}
