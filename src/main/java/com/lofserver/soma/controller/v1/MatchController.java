package com.lofserver.soma.controller.v1;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.service.api.match.MatchApiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@ApiResponses({
        @ApiResponse(code = 200, message = "success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class MatchController {
    private final MatchApiService matchApiService;
    private final JsonWebToken jsonWebToken;
    /*
    @ApiOperation(value = "Team vs Team 정보 반환 Api", notes = "client에서 team name들을 주면 server에서 해당 팀의 맞는 값들을 반환한다.",response = TeamVsTeam.class)
    @GetMapping("/teamVSteam")
    public ResponseEntity<?> getTeamVSTeam(@RequestParam(value = "matchId")Long matchId){
        return lofService.getTeamVsTeam(matchId);
    }*/

    @ApiOperation(value = "main", notes = "main",response = CommonItem[].class)
    @GetMapping("/mainPage")
    public ResponseEntity<?> getMainPage(@RequestHeader("Authorization") String token){
        if(jsonWebToken.checkJwtToken(token)){
            Long id = jsonWebToken.parseJwtToken(token).get("id",Long.class);
            return matchApiService.getMainPage(id);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");

    }
    @ApiOperation(value = "User의 Fandom에 맞는 경기 내역 반환 Api", notes = "client에서 User id를 주면 server에서 해당 유저의 맞는 경기들을 반환한다.",response = CommonItem.class)
    @GetMapping("/matchList")
    public ResponseEntity<?> getMatchList(@RequestHeader("Authorization") String token, @RequestParam(value = "all", required = false,defaultValue = "false")Boolean isAll, @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        if(jsonWebToken.checkJwtToken(token)){
            Long id = jsonWebToken.parseJwtToken(token).get("id",Long.class);
            return matchApiService.getMatchList(id, isAll, date);
        }
        else
            return ResponseEntity.badRequest().body("Invalid Token");
    }


    /*
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
    */
}
