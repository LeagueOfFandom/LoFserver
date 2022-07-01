package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.domain.TeamEntity;
import com.lofserver.soma.domain.UserEntity;
import com.lofserver.soma.domain.UserTeamListEntity;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserDeviceDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import com.lofserver.soma.repository.UserTeamlistRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LofService {


    private final TeamRepository teamRepository;
    private final UserTeamlistRepository userTeamlistRepository;
    private final UserRepository userRepository;

    private LofService(TeamRepository teamRepository, UserTeamlistRepository userTeamlistRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userTeamlistRepository = userTeamlistRepository;
        this.userRepository = userRepository;
    }

    public UserTeamInfoList makeUserTeamInfoList(Long userId){
        List<UserTeamInfo> userTeamInfoList = new ArrayList<>();

        List<TeamEntity> teamEntityList = teamRepository.findAll();
        List<UserTeamListEntity> userTeamList = userTeamlistRepository.findAllByUserId(userId);


        teamEntityList.forEach((teamEntity) -> {
            boolean teamCheck = false;
            for (int j = 0; j < userTeamList.size(); j++)
                if (teamEntity.getTeamId() == userTeamList.get(j).getTeamId())
                    teamCheck = true;

            userTeamInfoList.add(new UserTeamInfo(teamEntity, teamCheck));
        });

        return new UserTeamInfoList(userTeamInfoList);
    }

    public UserId makeUserId(UserDeviceDto userDeviceDto){
        UserEntity userEntity = userRepository.findUserEntityByDeviceId(userDeviceDto.getDeviceId());

        //등록되지 않은 유저라면
        if(userEntity == null)
            return new UserId(userRepository.save(new UserEntity(null, userDeviceDto.getDeviceId())).getUserId(), false);
        //등록된 유저라면
        return new UserId(userEntity.getUserId(), true);
    }

}
