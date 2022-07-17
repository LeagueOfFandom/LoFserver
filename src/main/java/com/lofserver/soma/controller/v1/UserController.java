package com.lofserver.soma.controller.v1;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.service.LofService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ApiResponses({
        @ApiResponse(code = 200, message = "!!Success!!"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final LofService lofService;
    @ApiOperation(value = "User의 Fandom에 맞는 경기 내역 반환 Api", notes = "client에서 User id를 주면 server에서 해당 유저의 맞는 경기들을 반환한다.")
    @GetMapping("/matchList")
    public MatchList getMatchList(@RequestParam(value = "id")Long userId, @RequestParam(value = "all")Boolean isAll){
        if(isAll == null) isAll = false;
        return lofService.getMatchList(userId, isAll);
    }
    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.")
    @GetMapping("/teamList")
    public UserTeamInfoList getTeamList(@RequestParam(value = "id")Long userId) {
        return lofService.getTeamList(userId);
    }
    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.")
    @PostMapping("/user")
    public UserId setUser(@RequestBody UserDto userDto){
        return lofService.setUser(userDto);
    }
    @ApiOperation(value = "유저의 Fandom list 설정 Api", notes = "client에서 User id와 함께 Fandom으로 설정한 팀들을 post하면 server에서 업데이트를 진행한다.")
    @PostMapping("/teamList")
    public String setTeamList(@RequestBody UserTeamListDto userTeamListDto){
        lofService.setTeamList(userTeamListDto);
        return "success";
    }
    @ApiOperation(value = "User의 경기 알람 설정 Api", notes = "client에서 User가 선택한 경기의 알람여부를 보내주면 server에서 업데이트를 진행한다.")
    @PostMapping("/alarm")
    public String setAlarm(@RequestBody UserAlarmDto userAlarmDto){
        lofService.setAlarm(userAlarmDto);
        return "success";
    }

}
