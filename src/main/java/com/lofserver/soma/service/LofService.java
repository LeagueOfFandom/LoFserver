package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.Roster;
import com.lofserver.soma.controller.v1.response.TeamVsTeam;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchDetails;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.controller.v1.response.teamRank.TeamRanking;
import com.lofserver.soma.controller.v1.response.teamRank.TeamRankingList;
import com.lofserver.soma.dto.TeamRankDto;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.TeamRankingEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.entity.match.MatchDetailsEntity;
import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private final TeamRankingRepository teamRankingRepository;

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
                matchListID.addAll(teamRepository.findById(teamId).orElse(null).getTeamMatchList());
            });

        Set<Long> matchListByTeam = new HashSet<>();
        teamList.forEach(teamId -> {
            matchListByTeam.addAll(teamRepository.findById(teamId).orElse(null).getTeamMatchList());
        });

        matchListID.forEach(matchId -> {
            MatchEntity matchEntity =  matchRepository.findById(matchId).orElse(null);
            //오늘 날짜를 기준으로 정렬.(이전경기 or 이후경기)
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
            if(userEntity.getTeamList().contains(teamEntity.getTeamId())) userTeamInfoList.add(new UserTeamInfo(teamEntity,true));
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

    public ResponseEntity<?> getTeamRankList(TeamRankDto teamRankDto){
        List<TeamRanking> teamRankingList = new ArrayList<>();

        List<TeamRankingEntity> teamRankingEntityList = teamRankingRepository.findByYearSeasonLeague(teamRankDto.getYear(), teamRankDto.getSeason(), teamRankDto.getLeague());

        if(teamRankingEntityList == null){
            log.info("getTeamRankList" + "해당 경기 없음");
            return new ResponseEntity<>("해당 경기 없음",HttpStatus.BAD_REQUEST);
        }

        for(TeamRankingEntity teamRankingEntity : teamRankingEntityList){
            //팀 name으로 찾기
            TeamEntity teamEntity = teamRepository.findNameByTeamName(teamRankingEntity.getTeamName());
            if(teamEntity == null){
                log.info("getTeamRankList" + "팀 이름 찾지 못함");
                return new ResponseEntity<>("팀 이름 찾지 못함",HttpStatus.BAD_REQUEST);
            }
            List<String> recentMatches = new ArrayList<>();
            List<MatchEntity> matchEntityList = matchRepository.findByTeamId(teamEntity.getTeamId());
            if(matchEntityList == null){
                log.info("getTeamRankList" + "최근 5경기 결과 불러오지 못함");
                return new ResponseEntity<>("최근 5경기 결과 불러오지 못함",HttpStatus.BAD_REQUEST);
            }
            for(MatchEntity matchEntity : matchEntityList){
                if(teamEntity.getTeamId() == matchEntity.getMatchInfo().getHomeTeamId()){
                    if(matchEntity.getHomeScore() == 2){ // 승리한 경우
                        recentMatches.add(0,"W");
                    }
                    else{
                        recentMatches.add(0,"L");
                    }
                }
                else if(teamEntity.getTeamId() == matchEntity.getMatchInfo().getAwayTeamId()){
                    if(matchEntity.getAwayScore() == 2){ // 승리한 경우
                        recentMatches.add(0,"W");
                    }
                    else{
                        recentMatches.add(0,"L");
                    }
                }
            }

            //팀 id와 사진 저장
            teamRankingList.add(new TeamRanking(teamRankingEntity, teamEntity.getTeamName(), teamEntity.getTeamImg(), recentMatches));
        }

        return new ResponseEntity<>(new TeamRankingList(teamRankingList), HttpStatus.OK);

    }

}
