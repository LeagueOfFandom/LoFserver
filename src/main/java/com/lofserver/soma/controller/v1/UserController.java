package com.lofserver.soma.controller.v1;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.matchDetail.TeamVsTeam;
import com.lofserver.soma.controller.v1.response.team.LeagueInfo;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.service.LofService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ApiResponses({
        @ApiResponse(code = 200, message = "success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final LofService lofService;
    private final JsonWebToken jsonWebToken;

    @ApiOperation(value = "Team vs Team 정보 반환 Api", notes = "client에서 team name들을 주면 server에서 해당 팀의 맞는 값들을 반환한다.",response = TeamVsTeam.class)
    @GetMapping("/teamVSteam")
    public ResponseEntity<?> getTeamVSTeam(@RequestParam(value = "matchId")Long matchId){
        return lofService.getTeamVsTeam(matchId);
    }
    @ApiOperation(value = "User의 Fandom에 맞는 경기 내역 반환 Api", notes = "client에서 User id를 주면 server에서 해당 유저의 맞는 경기들을 반환한다.",response = MatchList.class)
    @GetMapping("/matchList")
    public ResponseEntity<?> getMatchList(@RequestHeader("Authorization") String token, @RequestParam(value = "all", required = false,defaultValue = "false")Boolean isAll, @RequestParam(value = "isAfter")Boolean isAfter, @RequestParam(value = "page", required = false, defaultValue = "0")int page){
        if(jsonWebToken.checkJwtToken(token)){
            Long id = jsonWebToken.parseJwtToken(token).get("id",Long.class);
            return lofService.getMatchList(id, isAll, isAfter, page);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }

    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.", response = TeamInfo[].class)
    @GetMapping("/teamList/user")
    public ResponseEntity<?> getTeamListByUser(@RequestHeader("Authorization") String token) {
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return lofService.getTeamListByUser(id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }

    @ApiOperation(value = "유저가 선택한 팀 Api", notes = "client에서 User id를 주면 해당 User가 선택한 팀들의 List를 server에서 반환한다.", response = LeagueList.class)
    @GetMapping("/teamList")
    public ResponseEntity<?> getTeamList(@RequestHeader("Authorization") String token) {
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return lofService.getTeamList(id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.",response = UserId.class)
    @PostMapping("/user")
    public ResponseEntity<?> setUser(@RequestBody UserDto userDto){
        return lofService.setUser(userDto);
    }
    @ApiOperation(value = "유저 등록 Api", notes = "client에서 device id를 주면 server에서 User id와 함께 새로운 User는 false, 기존 User는 true를 반환한다.",response = UserId.class)
    @PostMapping("/fcm")
    public ResponseEntity<?> setFcm(@RequestHeader("Authorization") String token, @RequestBody String fcmToken){
        if (jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            return lofService.setFcm(fcmToken, id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "유저의 Fandom list 설정 Api", notes = "client에서 User id와 함께 Fandom으로 설정한 팀들을 post하면 server에서 업데이트를 진행한다.")
    @PostMapping("/teamList")
    public ResponseEntity<String> setTeamList(@RequestHeader("Authorization") String token, @RequestBody UserTeamListDto userTeamListDto){
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            if(id != userTeamListDto.getUserId())
                return ResponseEntity.badRequest().body("wrong user id");
            return lofService.setTeamList(userTeamListDto);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "User의 경기 알람 설정 Api", notes = "client에서 User가 선택한 경기의 알람여부를 보내주면 server에서 업데이트를 진행한다.")
    @PostMapping("/alarm")
    public ResponseEntity<String> setAlarm(@RequestHeader("Authorization") String token,@RequestBody UserAlarmDto userAlarmDto){
        if(jsonWebToken.checkJwtToken(token)) {
            Long id = jsonWebToken.parseJwtToken(token).get("id", Long.class);
            if (id != userAlarmDto.getUserId())
                return ResponseEntity.badRequest().body("wrong user id");
            return lofService.setAlarm(userAlarmDto);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }
    @ApiOperation(value = "리그 순위 Api", notes = "client에서 년도, 시즌, 리그를 보내주면 server에서 리그순위를 보내준다.")
    @GetMapping("/teamRankList")
    public ResponseEntity<?> setTeamRankList(@RequestParam(value="year")String year, @RequestParam(value="season")String season, @RequestParam(value="league")String league){
        return lofService.getTeamRankList(year, season, league);
    }
}
