package com.lofserver.soma.controller.v1;

import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDeviceDto;
import com.lofserver.soma.dto.UserIdDto;
import com.lofserver.soma.controller.v1.response.team.UserTeamInfoList;
import com.lofserver.soma.dto.fandom.UserFandomDto;
import com.lofserver.soma.service.LofService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@ApiResponses({
        @ApiResponse(code = 200, message = "!!Success!!"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController("/api/v1/team")
@Slf4j
public class UserController {
    @Autowired
    private LofService lofService;

    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.")
    @PostMapping("/getUserId")
    public UserId getUserId(@RequestBody UserDeviceDto userDeviceDto){
        return lofService.makeUserId(userDeviceDto);
    }

    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.")
    @PostMapping("/getUserTeamInfo")
    public UserTeamInfoList getUserTeamInfo(@RequestBody UserIdDto userIdDto) {
        return lofService.makeUserTeamInfoList(userIdDto.getUserId());
    }

    @ApiOperation(value = "유저의 Fandom list 설정 Api", notes = "client에서 User id와 함께 Fandom으로 설정한 팀들을 post하면 server에서 업데이트를 진행한다.")
    @PostMapping("/updateUserFandom")
    public void updateUserFandom(@RequestBody UserFandomDto userFandomDto){
    }

    @ApiOperation(value = "User의 Fandom에 맞는 경기 내역 반환 Api", notes = "client에서 User id를 주면 server에서 해당 유저의 맞는 경기들을 반환한다.")
    @PostMapping("/getUserMatches")
    public MatchList getMatchList(@RequestBody UserIdDto userIdDto){
        return new MatchList(new ArrayList<Match>());
    }

    @ApiOperation(value = "User의 경기 알람 설정 Api", notes = "client에서 User가 선택한 경기의 알람여부를 보내주면 server에서 업데이트를 진행한다.")
    @PostMapping("/updateUserAlarm")
    public void updateUserAlarm(@RequestBody UserAlarmDto userAlarmDto){

    }

}