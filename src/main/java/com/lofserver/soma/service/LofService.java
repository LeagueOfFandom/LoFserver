package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.TeamUserEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserTokenDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.TeamUserRepository;
import com.lofserver.soma.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LofService {


    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;

    public LofService(TeamRepository teamRepository, UserRepository userRepository, TeamUserRepository teamUserRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.teamUserRepository = teamUserRepository;
    }

    public UserTeamInfoList makeUserTeamInfoList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        List<UserTeamInfo> userTeamInfoList = new ArrayList<>();

        List<TeamEntity> teamEntityList = teamRepository.findAll();
        List<TeamEntity> userTeamEntityList = teamUserRepository.findTeamEntityByUserEntity(userEntity);

        teamEntityList.forEach(teamEntity -> {
            if(userTeamEntityList.contains(teamEntity)) userTeamInfoList.add(new UserTeamInfo(teamEntity, true));
            else userTeamInfoList.add(new UserTeamInfo(teamEntity, false));
        });

        return new UserTeamInfoList(userTeamInfoList);
    }

    public UserId makeUserId(UserTokenDto userTokenDto){
        UserEntity userEntity = userRepository.findUserEntityByDeviceId(userTokenDto.getToken());

        //등록되지 않은 유저라면
        if(userEntity == null)
            return new UserId(userRepository.save(new UserEntity(null, userTokenDto.getToken())).getUserId(), false);
        //등록된 유저라면
        return new UserId(userEntity.getUserId(), true);
    }

}
