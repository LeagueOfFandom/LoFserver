package com.lofserver.soma.service;

import com.lofserver.soma.domain.TeamEntity;
import com.lofserver.soma.domain.UserTeamlistEntity;
import com.lofserver.soma.dto.TeamDto;
import com.lofserver.soma.dto.UserTeamInfoDto;
import com.lofserver.soma.dto.UserTeamInfoListDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserTeamlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class LofService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamlistRepository userTeamlistRepository;

    @PostConstruct
    public UserTeamInfoListDto makeUserTeamInfoList(Long userId){
        List<UserTeamInfoDto> userTeamInfoDtoList = new ArrayList<>();
        UserTeamInfoListDto userTeamInfoListDto = new UserTeamInfoListDto(userTeamInfoDtoList);

        List<TeamEntity> teamEntities = teamRepository.findAll();
        List<UserTeamlistEntity> userTeamlistEntities = userTeamlistRepository.findAllByUserId(userId);


        for(int i = 0; i < teamEntities.size(); i++) {

            boolean teamCheck = false;
            Long teamId = teamEntities.get(i).getTeamId();

            //유저가 선택한 팀 인지 판단하기.
            for(int j = 0; j < userTeamlistEntities.size(); j++)
                if(teamId == userTeamlistEntities.get(j).getTeamId())
                    teamCheck = true;

            userTeamInfoListDto.addTeamInfo(new UserTeamInfoDto(teamEntities.get(i),teamCheck));
        }

        return userTeamInfoListDto;
    }

}
