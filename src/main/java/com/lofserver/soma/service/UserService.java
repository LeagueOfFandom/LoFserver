package com.lofserver.soma.service;

import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

        if(userEntity.getTeamList().contains(homeTeamId))
            return true;
        else if(userEntity.getTeamList().contains(awayTeamId))
            return true;
        else if(userEntity.getUserSelected().containsKey(matchEntity.getId()) && userEntity.getUserSelected().get(matchEntity.getId()))
            return true;
        else
            return false;
    }

    public List<Long> getUserLeagueList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + userId));
        return userEntity.getLeagueList();
    }
}
