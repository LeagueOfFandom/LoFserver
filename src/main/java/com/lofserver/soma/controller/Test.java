package com.lofserver.soma.controller;

import com.lofserver.soma.domain.TeamEntity;
import com.lofserver.soma.domain.UserTeamlistEntity;
import com.lofserver.soma.dto.UserIdDto;
import com.lofserver.soma.dto.UserTeamInfoDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserTeamlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class Test {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamlistRepository userTeamlistRepository;

    @GetMapping("/test")
    public List<UserTeamInfoDto> test() {

        List<UserTeamInfoDto> userTeamInfoListDto = new ArrayList<>(); // 유저의 관심 팀 넣을 리스트
        List<TeamEntity> teamEntities = teamRepository.findAll(); // 모든 팀 정보
        List<UserTeamlistEntity> userTeamlistEntities = userTeamlistRepository.findAllByUserId(1L); // 유저 아이디로 검색

        for (int i = 0; i < teamEntities.size(); i++) {
            boolean teamCheck = false;
            Long teamId = teamEntities.get(i).getTeamId();

            //유저가 선택한 팀 인지 판단하기.
            for (int j = 0; j < userTeamlistEntities.size(); j++)
                if (teamId == userTeamlistEntities.get(j).getTeamId())
                    teamCheck = true;

            userTeamInfoListDto.add(new UserTeamInfoDto(teamEntities.get(i), teamCheck));
        }

        return userTeamInfoListDto;
    }
}