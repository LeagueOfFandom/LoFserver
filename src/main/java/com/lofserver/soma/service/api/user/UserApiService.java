package com.lofserver.soma.service.api.user;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamInfo;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.google.GoogleUserInfoDto;
import com.lofserver.soma.service.league.LeagueRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiService {

    private final GoogleAccessService googleAccessService;
    private final UserRepositoryService userRepositoryService;
    private final LeagueRepositoryService leagueRepositoryService;
    private final JsonWebToken jsonWebToken;

    public ResponseEntity<?> setUser(UserDto userDto){
        GoogleUserInfoDto googleUserInfoDto = googleAccessService.getUserInfo(userDto.getGoogleAccessToken());
        if(googleUserInfoDto == null)
            return new ResponseEntity<>("googleAccessToken is invalid", HttpStatus.BAD_REQUEST);

        Long userId = userRepositoryService.getUserIdByEmail(googleUserInfoDto.getEmail());
        if(userId == null) {
            userId = userRepositoryService.createUserByUserDtoAndGoogleDto(userDto,googleUserInfoDto);
            return new ResponseEntity<>(new UserId(jsonWebToken.makeJwtTokenById(userId) , false), HttpStatus.OK);
        }
        else {
            String jwtToken = jsonWebToken.makeJwtTokenById(userId);
            return new ResponseEntity<>(new UserId(jwtToken, true), HttpStatus.OK);
        }
    }
    public ResponseEntity<?> getTeamListByUser(Long userId){

        List<Long> selectedTeamList = userRepositoryService.getUserTeamList(userId);
        List<TeamInfo> teamInfoList = leagueRepositoryService.getTeamInfoListByTeamIdList(selectedTeamList);

        return new ResponseEntity<>(teamInfoList, HttpStatus.OK);
    }
    public ResponseEntity<?> getAllTeamListByUser(Long userId){
        List<Long> selectedLeagueList = userRepositoryService.getUserLeagueList(userId);
        LeagueList leagueList = leagueRepositoryService.getLeagueListByLeagueIdList(selectedLeagueList);

        return new ResponseEntity<>(leagueList, HttpStatus.OK);
    }
    public ResponseEntity<String> setUserTeamList(List<Long> userTeamListDto, Long id){
        userRepositoryService.setTeamList(id, userTeamListDto);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    public ResponseEntity<?> setUserFcm(String fcmToken, Long id){
        userRepositoryService.setFcmToken(id, fcmToken);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
