package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserIdDto;
import com.lofserver.soma.dto.UserMatchListDto;
import com.lofserver.soma.dto.UserTokenDto;
import com.lofserver.soma.dto.fandom.TeamId;
import com.lofserver.soma.dto.fandom.UserFandomDto;
import com.lofserver.soma.entity.*;
import com.lofserver.soma.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
public class LofService {


    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;
    private final UserSelectRepository userSelectRepository;
    private final MatchUserRepository matchUserRepository;
    private final MatchLckRepository matchLckRepository;

    public LofService(TeamRepository teamRepository, UserRepository userRepository, TeamUserRepository teamUserRepository, UserSelectRepository userSelectRepository, MatchUserRepository matchUserRepository, MatchLckRepository matchLckRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamUserRepository = teamUserRepository;
        this.userSelectRepository = userSelectRepository;
        this.matchUserRepository = matchUserRepository;
        this.matchLckRepository = matchLckRepository;
    }

    //userid에 따른 선택한 fandom list 반환
    public UserTeamInfoList makeUserTeamInfoList(Long userId){
        List<UserTeamInfo> userTeamInfoList = new ArrayList<>();

        List<TeamEntity> teamEntityList = teamRepository.findAll();
        List<TeamEntity> userTeamEntityList = teamUserRepository.findTeamEntityByUserId(userId);

        teamEntityList.forEach(teamEntity -> {
            if(userTeamEntityList.contains(teamEntity)) userTeamInfoList.add(new UserTeamInfo(teamEntity, true));
            else userTeamInfoList.add(new UserTeamInfo(teamEntity, false));
        });

        return new UserTeamInfoList(userTeamInfoList);
    }

    //token에 따른 userid 반환.
    public UserId makeUserId(UserTokenDto userTokenDto){
        UserEntity userEntity = userRepository.findUserEntityByDeviceId(userTokenDto.getDeviceId());

        //등록되지 않은 유저라면
        if(userEntity == null)
            return new UserId(userRepository.save(new UserEntity(null, userTokenDto.getToken(), userTokenDto.getDeviceId())).getUserId(), false);
        //등록된 유저라면
        userEntity.setToken(userTokenDto.getToken());
        userRepository.flush();
        return new UserId(userEntity.getUserId(), true);
    }

    //알람 변경 여부에 따른 유저 변경정보 저장 및 경기별 알람유저 변경.
    public void setMatchAlarm(UserAlarmDto userAlarmDto){
        UserSelectEntity userSelectEntity = userSelectRepository.findByMatchId(userAlarmDto.getMatchId());

        //유저가 변경하였다는 정보 저장
        if(userSelectEntity == null)
            userSelectRepository.save(new UserSelectEntity(null, userRepository.findById(userAlarmDto.getUserId()).orElse(null), userAlarmDto.getMatchId(),userAlarmDto.getAlarm()));
        else {
            userSelectEntity.setAlarm(userAlarmDto.getAlarm());
            userSelectRepository.flush();
        }
        setMatchUserAlarm(userAlarmDto);
    }

    public void setMatchUserAlarm(UserAlarmDto userAlarmDto){
        MatchLckEntity matchLckEntity = matchLckRepository.findById(userAlarmDto.getMatchId()).orElse(null);
        UserEntity userEntity = userRepository.findById(userAlarmDto.getUserId()).orElse(null);
        if(userAlarmDto.getAlarm()){
            if(matchUserRepository.findByMatchLckEntityAndUserEntity(matchLckEntity, userEntity) == null)
                matchUserRepository.save(new MatchUserEntity(null, matchLckEntity,userEntity));
        }
        else{
            matchUserRepository.deleteMatchUserEntityByMatchLckEntityAndUserEntity(matchLckEntity, userEntity);
        }
    }

    //user의 새로운 fandom list로 변경. 그로인한 알람 또한 변경.
    public void setFandomList(UserFandomDto userFandomDto){
        UserEntity userEntity = userRepository.findById(userFandomDto.getUserId()).orElse(null);
        List<UserSelectEntity> userSelectedMatchIdList = userSelectRepository.findAllByUserEntity(userEntity);
        List<TeamEntity> teamEntityList = teamUserRepository.findTeamEntityByUserId(userFandomDto.getUserId());
        List<TeamId> teamIdList = userFandomDto.getTeamIdList();

        //기존 fandom 삭제
        teamEntityList.forEach(teamEntity -> {
            log.info(teamEntity.toString());
            teamUserRepository.deleteTeamUserEntitiesByUserEntity(userEntity);
            matchUserRepository.deleteMatchUserEntitiesByMatchLckEntity_HomeIdOrMatchLckEntity_AwayId(teamEntity, teamEntity);
        });

        HashSet<MatchLckEntity> matchLckEntityList = new HashSet<>();
        //새로운 fandom 추가
        teamIdList.forEach(teamId -> {
            TeamEntity teamEntity = teamRepository.findById(teamId.getTeamId()).orElse(null);
            teamUserRepository.save(new TeamUserEntity(null, userEntity, teamEntity));

            matchLckEntityList.addAll(teamEntity.getHomeMatchLckEntityList());
            matchLckEntityList.addAll(teamEntity.getAwayMatchLckEntityList());
        });
        matchLckEntityList.forEach(matchLckEntity -> {
            matchUserRepository.save(new MatchUserEntity(null, matchLckEntity, userEntity));
        });

        //사용자 설정 덮어쓰기
        userSelectedMatchIdList.forEach(userSelectEntity -> {
            setMatchUserAlarm(new UserAlarmDto(userFandomDto.getUserId(), userSelectEntity.getMatchId(), userSelectEntity.getAlarm()));
        });
    }

    //user의 fandom list의 따른 경기 내역제공
    public MatchList getUserMatchList(UserMatchListDto userMatchListDto){
        UserEntity userEntity = userRepository.findById(userMatchListDto.getUserId()).orElse(null);
        List<TeamEntity> teamEntityList = teamUserRepository.findTeamEntityByUserId(userMatchListDto.getUserId());
        HashSet<MatchLckEntity> matchLckEntityList = new HashSet<>();
        List<Match> matchList = new ArrayList<>();

        //팀을 선택 안했다면 전부 보내준다.
        if(teamEntityList.isEmpty() || userMatchListDto.getIsAll()){
            teamEntityList = teamRepository.findAll();
        }

        //중복 없이 해당 fandom의 경기를 배열에 저장.
        teamEntityList.forEach(teamEntity -> {
            matchLckEntityList.addAll(teamEntity.getHomeMatchLckEntityList());
            matchLckEntityList.addAll(teamEntity.getAwayMatchLckEntityList());
        });

        matchLckEntityList.forEach(matchLckEntity -> {
            if(matchUserRepository.findByMatchLckEntityAndUserEntity(matchLckEntity, userEntity) != null)
                matchList.add(matchLckEntity.toMatch(false,"add after",true));
            else matchList.add(matchLckEntity.toMatch(false,"add after",false));
        });

        Collections.sort(matchList, new Comparator<Match>() {
            @Override
            public int compare(Match o1, Match o2) {
                if(o1.getMatchDate().compareTo(o2.getMatchDate()) == 0)
                    return o1.getMatchTime().compareTo(o2.getMatchTime());
                return o1.getMatchDate().compareTo(o2.getMatchDate());
            }
        });


        return new MatchList(matchList);
    }
}
