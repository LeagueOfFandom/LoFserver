package com.lofserver.soma.controller.v1;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.service.api.user.UserApiService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserApiService userApiService;
    private final JsonWebToken jsonWebToken;
    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.", response = TeamInfo[].class)
    @GetMapping("/teamList/user")
    public ResponseEntity<?> getTeamListByUser(@RequestHeader("Authorization") String token) {
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return userApiService.getTeamListByUser(id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }

    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.", response = LeagueList.class)
    @GetMapping("/teamList")
    public ResponseEntity<?> getAllTeamListByUser(@RequestHeader("Authorization") String token) {
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return userApiService.getAllTeamListByUser(id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.",response = UserId.class)
    @PostMapping("/user")
    public ResponseEntity<?> setUser(@RequestBody UserDto userDto){
        return userApiService.setUser(userDto);
    }
    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.",response = UserId.class)
    @PostMapping("/fcm")
    public ResponseEntity<?> setUserFcm(@RequestHeader("Authorization") String token, @RequestBody String fcmToken){
        if (jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return userApiService.setUserFcm(fcmToken, id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "유저의 Fandom list 설정 Api", notes = "client에서 User id와 함께 Fandom으로 설정한 팀들을 post하면 server에서 업데이트를 진행한다.")
    @PostMapping("/teamList")
    public ResponseEntity<String> setTeamList(@RequestHeader("Authorization") String token, @RequestBody List<Long> userTeamListDto){
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return userApiService.setUserTeamList(userTeamListDto, id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
}
