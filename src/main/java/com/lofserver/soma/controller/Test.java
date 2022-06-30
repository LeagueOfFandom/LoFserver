package com.lofserver.soma.controller;

import com.lofserver.soma.dto.UserTeamInfoListDto;
import com.lofserver.soma.service.LofService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor // service might not have been initialized 방지
public class Test {

    private final LofService lofService;

    @GetMapping("/test")
    public UserTeamInfoListDto Test() {
        UserTeamInfoListDto userTeamInfoListDto = new UserTeamInfoListDto();
        userTeamInfoListDto = lofService.makeUserTeamInfoList(1L);
        return userTeamInfoListDto;
    }
}