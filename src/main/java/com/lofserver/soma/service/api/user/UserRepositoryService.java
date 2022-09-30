package com.lofserver.soma.service.api.user;

import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.google.GoogleUserInfoDto;
import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.repository.UserRepository;
import com.lofserver.soma.service.league.LeagueRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRepositoryService {

    private final UserRepository userRepository;
    private final LeagueRepositoryService leagueRepositoryService;

    public List<Long> getLeagueIdListByUserId(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity == null)
            return null;

        return userEntity.getLeagueList();
    }

    public Long createUserByUserDtoAndGoogleDto(UserDto userDto, GoogleUserInfoDto googleDto){
        UserEntity userEntity = UserEntity.builder()
                .token(userDto.getFcmToken())
                .email(googleDto.getEmail())
                .nickname(googleDto.getName())
                .profileImg(googleDto.getPicture())
                .build();
        return userRepository.save(userEntity).getUserId();
    }

    public Long getUserIdByEmail(String googleEmail){
        UserEntity userEntity = userRepository.findByEmail(googleEmail);
        if (userEntity == null)
            return null;
        else
            return userEntity.getUserId();
    }

    public Boolean checkUserMatchSetAlarmByMatchEntity(Long userId, MatchEntity matchEntity) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        Long homeTeamId = matchEntity.getOpponents().get(0).getOpponent().getId();
        Long awayTeamId = matchEntity.getOpponents().get(1).getOpponent().getId();

        if(userEntity.getUserSelected().containsKey(matchEntity.getId()))
            return userEntity.getUserSelected().get(matchEntity.getId());
        else if (userEntity.getTeamList().contains(homeTeamId) || userEntity.getTeamList().contains(awayTeamId))
            return true;
        else
            return false;
    }

    public Boolean checkUserTeamByMatchEntity(Long userId, MatchEntity matchEntity){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));

        Long homeTeamId = matchEntity.getOpponents().get(0).getOpponent().getId();
        Long awayTeamId = matchEntity.getOpponents().get(1).getOpponent().getId();

        if(userEntity.getTeamList().contains(homeTeamId) || userEntity.getTeamList().contains(awayTeamId))
            return true;
        else if(userEntity.getUserSelected().containsKey(matchEntity.getId()) && userEntity.getUserSelected().get(matchEntity.getId()))
            return true;
        else
            return false;
    }

    public void setFcmToken(Long userId, String fcmToken){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        userEntity.setToken(fcmToken);
        userRepository.save(userEntity);
    }

    public void setTeamList(Long userId, List<Long> teamList){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        userEntity.setTeamList(teamList);
        userEntity.setLeagueList(leagueRepositoryService.getLeagueIdListByTeamIdList(teamList));
        userRepository.save(userEntity);
    }

    public List<Long> getUserTeamList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return userEntity.getTeamList();
    }

    public List<Long> getUserLeagueList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return userEntity.getLeagueList();
    }
}
