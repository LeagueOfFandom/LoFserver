package com.lofserver.soma.controller;

import com.lofserver.soma.domain.TeamEntity;
import com.lofserver.soma.domain.UserTeamlistEntity;
import com.lofserver.soma.dto.UserIdDto;
import com.lofserver.soma.dto.UserTeamInfoDto;
import com.lofserver.soma.dto.UserTeamInfoListDto;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserTeamlistRepository;
import com.lofserver.soma.service.LofService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@ApiResponses({
        @ApiResponse(code = 200, message = "!!Success!!"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@Slf4j
public class Test {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserTeamlistRepository userTeamlistRepository;
    @ApiOperation(value = "테스트 고고", notes = "가즈아")
    @PostMapping("/test")
    public List<UserTeamInfoDto> test(@RequestBody UserIdDto userIdDto){
        List<UserTeamInfoDto> userTeamInfoListDto = new ArrayList<>();

        List<TeamEntity> teamEntities = teamRepository.findAll();
        List<UserTeamlistEntity> userTeamlistEntities = userTeamlistRepository.findAllByUserId(userIdDto.getUserId());


        for(int i = 0; i < teamEntities.size(); i++) {

            boolean teamCheck = false;
            Long teamId = teamEntities.get(i).getTeamId();

            //유저가 선택한 팀 인지 판단하기.
            for(int j = 0; j < userTeamlistEntities.size(); j++)
                if(teamId == userTeamlistEntities.get(j).getTeamId())
                    teamCheck = true;

            userTeamInfoListDto.add(new UserTeamInfoDto(teamEntities.get(i),teamCheck));
            log.info(userTeamInfoListDto.toString());
        }

        return userTeamInfoListDto;
    }
}
