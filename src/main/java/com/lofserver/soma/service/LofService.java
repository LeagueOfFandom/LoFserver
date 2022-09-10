package com.lofserver.soma.service;

import com.lofserver.soma.config.JsonWebToken;
import com.lofserver.soma.controller.v1.response.Roster;
import com.lofserver.soma.controller.v1.response.UserId;
import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchDetails;
import com.lofserver.soma.controller.v1.response.match.MatchList;
import com.lofserver.soma.controller.v1.response.matchDetail.*;
import com.lofserver.soma.controller.v1.response.team.LeagueInfo;
import com.lofserver.soma.controller.v1.response.team.LeagueList;
import com.lofserver.soma.controller.v1.response.team.TeamList;
import com.lofserver.soma.controller.v1.response.teamRank.TeamRanking;
import com.lofserver.soma.controller.v1.response.teamRank.TeamRankingList;
import com.lofserver.soma.data.DragonImgs;
import com.lofserver.soma.dto.TeamRankDto;
import com.lofserver.soma.dto.UserAlarmDto;
import com.lofserver.soma.dto.UserDto;
import com.lofserver.soma.dto.UserTeamListDto;
import com.lofserver.soma.dto.crawlDto.gameDto.sub.player.Player;
import com.lofserver.soma.dto.crawlDto.gameDto.sub.teams.Team;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.Game;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.Opponent;
import com.lofserver.soma.dto.google.GoogleUserInfoDto;
import com.lofserver.soma.entity.*;
import com.lofserver.soma.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class LofService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    private final MatchDetailRepository matchDetailRepository;
    private final TeamRankingRepository teamRankingRepository;
    private final ChampionRepository championRepository;
    private final LeagueRepository leagueRepository;
    private final JsonWebToken jsonWebToken;

    public ResponseEntity<?> setFcm(String fcmToken, Long id){
        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if(userEntity == null){
            return new ResponseEntity<>("User Not Found", HttpStatus.BAD_REQUEST);
        }
        userEntity.setToken(fcmToken);
        userRepository.save(userEntity);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    private TeamVsTeamSetInfo setKDA(MatchDetailEntity matchDetailEntity) {
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "KDA";
            Opponent blueTeam = null, redTeam = null;
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0).getTeam();
                redTeam = matchDetailEntity.getTeams().get(1).getTeam();
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1).getTeam();
                redTeam = matchDetailEntity.getTeams().get(0).getTeam();
            }
            Long blueKills = 0L, blueDeaths = 0L, blueAssists = 0L;
            Long redKills = 0L, redDeaths = 0L, redAssists = 0L;
            for(Player player :matchDetailEntity.getPlayers()){
                if(player.getTeam().getId().equals(blueTeam.getId())){
                    blueKills += player.getKills();
                    blueDeaths += player.getDeaths();
                    blueAssists += player.getAssists();
                }
                else{
                    redKills += player.getKills();
                    redDeaths += player.getDeaths();
                    redAssists += player.getAssists();
                }
            }
            String blueKDA = blueKills + "/" + blueDeaths + "/" + blueAssists;
            String redKDA = redKills + "/" + redDeaths + "/" + redAssists;
            StringObject stringObject = new StringObject(text, blueKDA, redKDA);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "KDA";
            TeamEntity blueTeamEntity = null, redTeamEntity = null;
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeamEntity = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null);
                redTeamEntity = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null);
            }
            else{
                blueTeamEntity = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null);
                redTeamEntity = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null);
            }
            Long blueKills = Double.doubleToLongBits(blueTeamEntity.getStatus().getAverages().getKills());
            Long blueDeaths = Double.doubleToLongBits(blueTeamEntity.getStatus().getAverages().getDeaths());
            Long blueAssists = Double.doubleToLongBits(blueTeamEntity.getStatus().getAverages().getAssists());

            Long redKills = Double.doubleToLongBits(redTeamEntity.getStatus().getAverages().getKills());
            Long redDeaths = Double.doubleToLongBits(redTeamEntity.getStatus().getAverages().getDeaths());
            Long redAssists = Double.doubleToLongBits(redTeamEntity.getStatus().getAverages().getAssists());

            String blueKDA = blueKills + "/" + blueDeaths + "/" + blueAssists;
            String redKDA = redKills + "/" + redDeaths + "/" + redAssists;
            StringObject stringObject = new StringObject(text, blueKDA, redKDA);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private TeamVsTeamSetInfo setGold(MatchDetailEntity matchDetailEntity){
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "GOLD";
            String blueGold = "", redGold = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueGold = (float)Math.round((float)matchDetailEntity.getTeams().get(0).getGold_earned() / 100) /10 + "k";
                redGold = (float)Math.round((float)matchDetailEntity.getTeams().get(1).getGold_earned() / 100 ) /10+ "k";
            }
            else{
                blueGold = (float)Math.round((float)matchDetailEntity.getTeams().get(1).getGold_earned() / 100) /10 + "k";
                redGold = (float)Math.round((float)matchDetailEntity.getTeams().get(0).getGold_earned() / 100) /10+ "k";
            }
            StringObject stringObject = new StringObject(text, blueGold, redGold);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "GOLD";
            String blueGold = "", redGold = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                Double blueGoldEarned = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getGold_earned();
                Double redGoldEarned = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getGold_earned();
                blueGold = (float)Math.round(blueGoldEarned / 100) /10+ "k";
                redGold = (float)Math.round(redGoldEarned / 100) /10+ "k";
            }
            else{
                Double blueGoldEarned = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getGold_earned();
                Double redGoldEarned = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getGold_earned();
                blueGold = (float)Math.round(blueGoldEarned / 100) /10+ "k";
                redGold = (float)Math.round(redGoldEarned / 100) /10+ "k";
            }
            StringObject stringObject = new StringObject(text, blueGold, redGold);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }
    private TeamVsTeamSetInfo setTower(MatchDetailEntity matchDetailEntity){
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "TOWERS";
            String blueTower = "", redTower = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTower = matchDetailEntity.getTeams().get(0).getTower_kills() + "";
                redTower = matchDetailEntity.getTeams().get(1).getTower_kills() + "";
            }
            else{
                blueTower = matchDetailEntity.getTeams().get(1).getTower_kills() + "";
                redTower = matchDetailEntity.getTeams().get(0).getTower_kills() + "";
            }
            StringObject stringObject = new StringObject(text, blueTower, redTower);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "TOWERS";
            String blueTower = "", redTower = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTower = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getTower_kills() + "";
                redTower = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getTower_kills() + "";
            }
            else{
                blueTower = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getTower_kills() + "";
                redTower = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getTower_kills() + "";
            }
            StringObject stringObject = new StringObject(text, blueTower, redTower);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private List<String> addByCount(List<String> list, String url,Long count){
        for(int i = 0; i < count; i++)
            list.add(url);
        return list;
    }

    private TeamVsTeamSetInfo setDrakes(MatchDetailEntity matchDetailEntity){
        DragonImgs dragonImgs = new DragonImgs();
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_IMAGE_VIEW";
            String text = "DRAKES";
            Team blueTeam = null, redTeam = null;
            List<String> blueDrakes = new ArrayList<>(), redDrakes = new ArrayList<>();
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }

            blueDrakes = addByCount(blueDrakes, dragonImgs.getHextech_drake(), blueTeam.getHextech_drake_kills());
            blueDrakes = addByCount(blueDrakes, dragonImgs.getCloud_drake(), blueTeam.getCloud_drake_kills());
            blueDrakes = addByCount(blueDrakes, dragonImgs.getInfernal_drake(), blueTeam.getInfernal_drake_kills());
            blueDrakes = addByCount(blueDrakes, dragonImgs.getMountain_drake(), blueTeam.getMountain_drake_kills());
            blueDrakes = addByCount(blueDrakes, dragonImgs.getOcean_drake(), blueTeam.getOcean_drake_kills());

            redDrakes = addByCount(redDrakes, dragonImgs.getHextech_drake(), redTeam.getHextech_drake_kills());
            redDrakes = addByCount(redDrakes, dragonImgs.getCloud_drake(), redTeam.getCloud_drake_kills());
            redDrakes = addByCount(redDrakes, dragonImgs.getInfernal_drake(), redTeam.getInfernal_drake_kills());
            redDrakes = addByCount(redDrakes, dragonImgs.getMountain_drake(), redTeam.getMountain_drake_kills());
            redDrakes = addByCount(redDrakes, dragonImgs.getOcean_drake(), redTeam.getOcean_drake_kills());

            ImgObject imgObject = new ImgObject(text, blueDrakes, redDrakes);
            return new TeamVsTeamSetInfo(viewType, null, imgObject);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "DRAKES";
            String blueDrakes = "", redDrakes = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueDrakes = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getDragon_kills() + "";
                redDrakes = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getDragon_kills() + "";
            }
            else{
                blueDrakes = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getDragon_kills() + "";
                redDrakes = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getDragon_kills() + "";
            }

            StringObject stringObject = new StringObject(text, blueDrakes, redDrakes);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private TeamVsTeamSetInfo setHeralds(MatchDetailEntity matchDetailEntity){
        DragonImgs dragonImgs = new DragonImgs();
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_IMAGE_VIEW";
            String text = "HERALDS";
            Team blueTeam = null, redTeam = null;
            List<String> blueHeralds = new ArrayList<>(), redHeralds = new ArrayList<>();
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            for(int i = 0; i < blueTeam.getHerald_kills(); i++)
                blueHeralds.add(dragonImgs.getHerald());
            for(int i = 0; i < redTeam.getHerald_kills(); i++)
                redHeralds.add(dragonImgs.getHerald());

            ImgObject imgObject = new ImgObject(text, blueHeralds, redHeralds);
            return new TeamVsTeamSetInfo(viewType, null, imgObject);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "HERALDS";
            String blueHeralds = "", redHeralds = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueHeralds = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getHerald_kill() + "";
                redHeralds = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getHerald_kill() + "";
            }
            else{
                blueHeralds = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getHerald_kill() + "";
                redHeralds = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getHerald_kill() + "";
            }

            StringObject stringObject = new StringObject(text, blueHeralds, redHeralds);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private TeamVsTeamSetInfo setElders(MatchDetailEntity matchDetailEntity){
        DragonImgs dragonImgs = new DragonImgs();
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_IMAGE_VIEW";
            String text = "ELDERS";
            Team blueTeam = null, redTeam = null;
            List<String> blueElders = new ArrayList<>(), redElders = new ArrayList<>();
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            for(int i = 0; i < blueTeam.getElder_drake_kills(); i++)
                blueElders.add(dragonImgs.getElder_drake());
            for(int i = 0; i < redTeam.getElder_drake_kills(); i++)
                redElders.add(dragonImgs.getElder_drake());

            ImgObject imgObject = new ImgObject(text, blueElders, redElders);
            return new TeamVsTeamSetInfo(viewType, null, imgObject);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "FIRST DRAKE";
            String blueElders = "", redElders = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueElders = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getRatios().getFirst_dragon() + "";
                redElders = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getRatios().getFirst_dragon() + "";
            }
            else{
                blueElders = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getRatios().getFirst_dragon() + "";
                redElders = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getRatios().getFirst_dragon() + "";
            }

            StringObject stringObject = new StringObject(text, blueElders, redElders);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private TeamVsTeamSetInfo setBarons(MatchDetailEntity matchDetailEntity){
        DragonImgs dragonImgs = new DragonImgs();
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_IMAGE_VIEW";
            String text = "BARONS";
            Team blueTeam = null, redTeam = null;
            List<String> blueBarons = new ArrayList<>(), redBarons = new ArrayList<>();
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            for(int i = 0; i < blueTeam.getBaron_kills(); i++)
                blueBarons.add(dragonImgs.getBaron());
            for(int i = 0; i < redTeam.getBaron_kills(); i++)
                redBarons.add(dragonImgs.getBaron());

            ImgObject imgObject = new ImgObject(text, blueBarons, redBarons);
            return new TeamVsTeamSetInfo(viewType, null, imgObject);
        }
        else{
            String viewType = "MATCH_INFO_STRING_VIEW";
            String text = "BARONS";
            String blueBarons = "", redBarons = "";
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueBarons = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getBaron_kills() + "";
                redBarons = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getBaron_kills() + "";
            }
            else{
                blueBarons = teamRepository.findById(matchDetailEntity.getTeams().get(1).getTeam().getId()).orElse(null).getStatus().getAverages().getBaron_kills() + "";
                redBarons = teamRepository.findById(matchDetailEntity.getTeams().get(0).getTeam().getId()).orElse(null).getStatus().getAverages().getBaron_kills() + "";
            }

            StringObject stringObject = new StringObject(text, blueBarons, redBarons);
            return new TeamVsTeamSetInfo(viewType, stringObject, null);
        }
    }

    private TeamVsTeamSetInfo setBans(MatchDetailEntity matchDetailEntity){
        DragonImgs dragonImgs = new DragonImgs();
        if(matchDetailEntity.getStatus().equals("finished")) {
            String viewType = "MATCH_INFO_IMAGE_VIEW";
            String text = "BANS";
            Team blueTeam = null, redTeam = null;
            List<String> blueBans = new ArrayList<>(), redBans = new ArrayList<>();
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            for(int i = 0; i < blueTeam.getBans().size(); i++){
                ChampionEntity champion = championRepository.findById(blueTeam.getBans().get(i)).orElse(null);
                if(champion != null)
                    blueBans.add(champion.getImgUrl());
                else
                    log.info(blueTeam.getBans().get(i) + "");
            }
            for(int i = 0; i < redTeam.getBaron_kills(); i++){
                ChampionEntity champion = championRepository.findById(redTeam.getBans().get(i)).orElse(null);
                if(champion != null)
                    redBans.add(champion.getImgUrl());
                else log.info(redTeam.getBans().get(i) + "");
            }
            ImgObject imgObject = new ImgObject(text, blueBans, redBans);
            return new TeamVsTeamSetInfo(viewType, null, imgObject);
        }
        return null;
    }

    public TeamVsTeamRosterInfo setRoster(MatchDetailEntity matchDetailEntity){
        if(matchDetailEntity.getStatus().equals("finished")) {
            Team blueTeam = null, redTeam = null;
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            List<Roster> blueRoster = new ArrayList<>(), redRoster = new ArrayList<>();
            for(Player player : matchDetailEntity.getPlayers()){
                String role = "https://d654rq93y7j8z.cloudfront.net/line/"+ player.getRole() + ".png";
                if(player.getTeam().getId().equals(blueTeam.getTeam().getId()))
                    blueRoster.add(new Roster(player.getPlayer_id(), player.getRole(), player.getPlayer().getName(), role, player.getPlayer().getImage_url()));
                else
                    redRoster.add(new Roster(player.getPlayer_id(), player.getRole(), player.getPlayer().getName(), role, player.getPlayer().getImage_url()));

            }
            return new TeamVsTeamRosterInfo(blueRoster, redRoster);
        }
        return null;
    }

    public TeamVsTeamMainInfo setScore(Map<Long, Long> score , MatchDetailEntity matchDetailEntity){
        if(matchDetailEntity.getStatus().equals("finished")) {
            if (!score.containsKey(matchDetailEntity.getTeams().get(0).getTeam().getId())) {
                score.put(matchDetailEntity.getTeams().get(0).getTeam().getId(), 0L);
                score.put(matchDetailEntity.getTeams().get(1).getTeam().getId(), 0L);
            }
            score.put(matchDetailEntity.getWinner().getId(), score.get(matchDetailEntity.getWinner().getId()) + 1);

            Team blueTeam = null, redTeam = null;
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeam = matchDetailEntity.getTeams().get(0);
                redTeam = matchDetailEntity.getTeams().get(1);
            }
            else{
                blueTeam = matchDetailEntity.getTeams().get(1);
                redTeam = matchDetailEntity.getTeams().get(0);
            }
            Long blueScore = score.get(blueTeam.getTeam().getId());
            Long redScore = score.get(redTeam.getTeam().getId());
            LocalDateTime localDateTime = matchDetailEntity.getBegin_at();
            String date = localDateTime.getYear() + "년 " + localDateTime.getMonthValue() + "월 " + localDateTime.getDayOfMonth() + "일";
            String time = localDateTime.getHour() + "시 " + localDateTime.getMinute() + "분";
            Boolean blueWin, redWin;
            if(matchDetailEntity.getWinner().getId().equals(blueTeam.getTeam().getId())){
                blueWin = true;
                redWin = false;
            }else {
                blueWin = false;
                redWin = true;
            }

            return new TeamVsTeamMainInfo(
                    date,
                    time,
                    blueTeam.getTeam().getAcronym(),
                    redTeam.getTeam().getAcronym(),
                    blueTeam.getTeam().getId(),
                    redTeam.getTeam().getId(),
                    blueTeam.getTeam().getImage_url(),
                    redTeam.getTeam().getImage_url(),
                    blueScore,
                    redScore,blueWin,redWin);

        }
        return null;
    }
    public TeamVsTeamPrediction setPre(MatchDetailEntity matchDetailEntity){
        Map<Long, Long> score = new HashMap<>();
        if(matchDetailEntity.getStatus().equals("finished")) {
            Long blueTeamId, redTeamId;
            if(matchDetailEntity.getTeams().get(0).getColor().equals("blue")){
                blueTeamId = matchDetailEntity.getTeams().get(0).getTeam().getId();
                redTeamId = matchDetailEntity.getTeams().get(1).getTeam().getId();
            }
            else{
                blueTeamId = matchDetailEntity.getTeams().get(1).getTeam().getId();
                redTeamId = matchDetailEntity.getTeams().get(0).getTeam().getId();
            }
            score.put(blueTeamId, 0L);
            score.put(redTeamId, 0L);
            List<MatchEntity> matchEntityList = matchRepository.findAllByTeamIds(blueTeamId, redTeamId);
            matchEntityList.forEach(matchEntity -> {
                score.put( matchEntity.getResults().get(0).getTeam_id(), score.get(matchEntity.getResults().get(0).getTeam_id()) + matchEntity.getResults().get(0).getScore());
                score.put( matchEntity.getResults().get(1).getTeam_id(), score.get(matchEntity.getResults().get(1).getTeam_id()) + matchEntity.getResults().get(1).getScore());
            });
            return new TeamVsTeamPrediction(score.get(blueTeamId), score.get(redTeamId));
        }
        return null;
    }



    public ResponseEntity<?> getTeamVsTeam(Long matchId){
        MatchEntity matchEntity = matchRepository.findById(matchId).orElse(null);
        if(matchEntity == null)
            return new ResponseEntity<>("해당 id 없음",HttpStatus.NOT_FOUND);

        List<TeamVsTeamDetails> teamVsTeamDetails = new ArrayList<>();
        Map<Long, Long> teamScore = new HashMap<>();
        for(Game game : matchEntity.getGames()){
            List<TeamVsTeamSetInfo>  teamVsTeamSetInfoList = new ArrayList<>();
            MatchDetailEntity matchDetailEntity = matchDetailRepository.findById(game.getId()).orElse(null);
            teamVsTeamSetInfoList.add(setKDA(matchDetailEntity));
            teamVsTeamSetInfoList.add(setGold(matchDetailEntity));
            teamVsTeamSetInfoList.add(setTower(matchDetailEntity));
            teamVsTeamSetInfoList.add(setHeralds(matchDetailEntity));
            teamVsTeamSetInfoList.add(setDrakes(matchDetailEntity));
            teamVsTeamSetInfoList.add(setElders(matchDetailEntity));
            teamVsTeamSetInfoList.add(setBarons(matchDetailEntity));

            TeamVsTeamRosterInfo teamVsTeamRosterInfo = setRoster(matchDetailEntity);
            TeamVsTeamMainInfo teamVsTeamMainInfo = setScore(teamScore, matchDetailEntity);
            TeamVsTeamPrediction teamVsTeamPrediction = setPre(matchDetailEntity);
            teamVsTeamDetails.add(new TeamVsTeamDetails(teamVsTeamSetInfoList, teamVsTeamRosterInfo, teamVsTeamMainInfo, teamVsTeamPrediction));
        }
        return new ResponseEntity<>(new TeamVsTeam(teamVsTeamDetails), HttpStatus.OK);
    }




    //user의 matchList반환 함수.
    public ResponseEntity<?> getMatchList(Long userId, Boolean isAll, Boolean isAfter, int page){
        final int PAGE_PER_LOCALDATE = 5;
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }


        List<Long> teamList = new ArrayList<>();
        if(isAll)
            teamList = teamRepository.findAllId();
        else
            teamList = userEntity.getTeamList();
        if(teamList.isEmpty())
            teamList = teamRepository.findAllId();

        TreeSet<Long> matchListID = new TreeSet<>();
        Map<Long, Boolean> userSelected = userEntity.getUserSelected();


        List<MatchEntity> matchEntityList = null;

        if (isAfter)
            matchEntityList = matchRepository.findAllAfterMatchByTeamIds(LocalDateTime.now().minusMonths(1), page * PAGE_PER_LOCALDATE, PAGE_PER_LOCALDATE, 8281L, teamList);
        else
            matchEntityList = matchRepository.findAllBeforeMatchByTeamIds(LocalDateTime.now().minusMonths(1), page * PAGE_PER_LOCALDATE, PAGE_PER_LOCALDATE, 8281L, teamList);


        List<MatchDetails> liveList = new ArrayList<>();
        List<MatchDetails> matchList = new ArrayList<>();
        matchEntityList.forEach(matchEntity -> {

            //오늘 날짜를 기준으로 정렬.(이전경기 or 이후경기)
            if(matchEntity.getOriginal_scheduled_at() == null) return;

            //설정한 값이 있다면 설정한 값으로 진행한다.
            if(userSelected.containsKey(matchEntity.getId())) {
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(userSelected.get(matchEntity.getId())));
                else matchList.add(matchEntity.toMatchDetails(userSelected.get(matchEntity.getId())));
            }
            //선택한 팀이라면 알람을 보내준다.
            else if(userEntity.getTeamList().contains(matchEntity.getOpponents().get(0).getOpponent().getId()) || userEntity.getTeamList().contains(matchEntity.getOpponents().get(1).getOpponent().getId())){
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(true));
                else matchList.add(matchEntity.toMatchDetails(true));
            }
            //아니라면 안보낸다.
            else {
                if(matchEntity.getStatus().equals("live")) liveList.add(matchEntity.toMatchDetails(false));
                else matchList.add(matchEntity.toMatchDetails(false));
            }
        });

        List<Match> returnMatchList = new ArrayList<>();
        for(int i = 0; i < matchList.size() - 1; i++){
            if(matchList.get(i).getMatchDate().equals(matchList.get(i+1).getMatchDate())){
                List<MatchDetails> matchDetailsList = new ArrayList<>();
                matchDetailsList.add(matchList.get(i));
                matchDetailsList.add(matchList.get(i+1));
                returnMatchList.add(new Match(matchList.get(i).getMatchDate(), matchDetailsList));
                i++;
            }
            else{
                List<MatchDetails> matchDetailsList = new ArrayList<>();
                matchDetailsList.add(matchList.get(i));
                returnMatchList.add(new Match(matchList.get(i).getMatchDate(), matchDetailsList));
            }
        }
        return new ResponseEntity<>(new MatchList(new Match(LocalDate.now(ZoneId.of("Asia/Seoul")),liveList) ,returnMatchList,10,page), HttpStatus.OK);
    }

    //user가 선택한 team list 제공 함수.
    public ResponseEntity<?> getTeamList(Long userId){
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if(userEntity == null){ // id 없음 예외처리.
            log.info("getTeamList" + "해당 id 없음 :" + userId);
            return new ResponseEntity<>("해당 id 없음",HttpStatus.BAD_REQUEST);
        }
        List<String> leagues = new ArrayList<>();
        leagues.add("LCK");
        leagues.add("LPL");
        leagues.add("LEC");
        leagues.add("LCS");
        leagues.add("Worlds");
        leagues.add("Mid-Season Invitational");

        List<LeagueInfo> leagueInfos = new ArrayList<>();
        leagues.forEach(league -> {
            log.info(league);
            Long seriesId = leagueRepository.findByName(league).getLatest_series_id();
            List<TeamEntity> teamEntityList = teamRepository.findAllBySeries_Id(seriesId);
            LeagueInfo leagueInfo = new LeagueInfo(league);
            List<TeamList> teamLists = new ArrayList<>();

            teamEntityList.forEach(teamEntity -> {
                if(userEntity.getTeamList().contains(teamEntity.getId()))
                    teamLists.add(new TeamList(teamEntity, true, league));
                else
                    teamLists.add(new TeamList(teamEntity, false, league));
            });
            leagueInfo.setTeamList(teamLists);
            leagueInfos.add(leagueInfo);
        });
        return new ResponseEntity<>(new LeagueList(leagueInfos,leagues), HttpStatus.OK);
    }
    //초기 user set 함수.
    public ResponseEntity<?> setUser(UserDto userDto){

        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + userDto.getGoogleAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        GoogleUserInfoDto googleUserInfo;

        try {
            ResponseEntity<GoogleUserInfoDto> googleUserInfoDto = restTemplate.exchange(url, HttpMethod.GET, requestEntity, GoogleUserInfoDto.class);
            googleUserInfo = googleUserInfoDto.getBody();
        }catch (Exception e){
            log.info("googleLogin : {}",e);
            return new ResponseEntity<>("googleAccessToken 오류",HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = userRepository.findByEmail(googleUserInfo.getEmail());

        //없다면 저장한다.
        if(userEntity == null) {
            userEntity = new UserEntity(userDto.getFcmToken(),googleUserInfo.getEmail());
            String jwtToken = jsonWebToken.makeJwtTokenById(userRepository.save(userEntity).getUserId());
            return new ResponseEntity<>(new UserId(jwtToken , false), HttpStatus.OK);
        }
        //있다면 반환한다.
        String jwtToken = jsonWebToken.makeJwtTokenById(userEntity.getUserId());
        return new ResponseEntity<>(new UserId(jwtToken, true), HttpStatus.OK);
    }
    //user의 팀 정보 수정.
    public ResponseEntity<String> setTeamList(UserTeamListDto userTeamListDto){
        UserEntity userEntity = userRepository.findById(userTeamListDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("getMatchList: "+"해당 id({}) 없음" , userTeamListDto.getUserId());
            return new ResponseEntity<>("해당 user id 없음", HttpStatus.BAD_REQUEST);
        }
        if(!teamRepository.findAllId().containsAll(userTeamListDto.getTeamIdList())){
            log.info("getMatchList: "+"팀 id({}) 리스트 잘못됨" , userTeamListDto.getTeamIdList().toString());
            return new ResponseEntity<>("해당 team id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.setTeamList(userTeamListDto.getTeamIdList());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    public ResponseEntity<String> setAlarm(UserAlarmDto userAlarmDto){
        UserEntity userEntity = userRepository.findById(userAlarmDto.getUserId()).orElse(null);

        if(userEntity == null){ // id 없음 예외처리.
            log.info("setAlarm: " + "해당 id({}) 없음" , userAlarmDto.getUserId());
            return new ResponseEntity<>("해당 id 없음", HttpStatus.BAD_REQUEST);
        }
        if(matchRepository.findById(userAlarmDto.getMatchId()).orElse(null) == null){
            log.info("setAlarm: " + "해당 match id({}) 없음" + userAlarmDto.getMatchId());
            return new ResponseEntity<>("해당 match id 없음", HttpStatus.BAD_REQUEST);
        }
        userEntity.addUserSelected(userAlarmDto.getMatchId(),userAlarmDto.getAlarm());
        userRepository.save(userEntity);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    public ResponseEntity<?> getTeamRankList(String year, String season, String league){
        log.info("getTeamRankList 실행");
        TeamRankDto teamRankDto = new TeamRankDto(year, season, league);
        List<TeamRanking> teamRankingList = new ArrayList<>();

        // 모든 팀 순위
        List<TeamRankingEntity> teamRankingEntityList = teamRankingRepository.findByYearSeasonLeague(teamRankDto.getYear(), teamRankDto.getSeason(), teamRankDto.getLeague());
        if(teamRankingEntityList == null){
            log.info("getTeamRankList" + "해당 경기 없음");
            return new ResponseEntity<>("해당 경기 없음",HttpStatus.BAD_REQUEST);
        }

        for(TeamRankingEntity teamRankingEntity : teamRankingEntityList){
            TeamEntity teamEntity = new TeamEntity();
            if(teamRankingEntity.getTeamName().equals("Nongshim RedForce")) {
                teamEntity = teamRepository.findByTeamName("Nongshim Red Force");
            }
            else {
                teamEntity = teamRepository.findByTeamName(teamRankingEntity.getTeamName()); // 팀 찾기
            }
            if(teamEntity == null){
                log.info("getTeamRankList" + "팀 찾지 못함");
                return new ResponseEntity<>("팀 찾지 못함",HttpStatus.BAD_REQUEST);
            }

            List<String> recentMatches = new ArrayList<>();
            List<MatchEntity> matchEntityList = matchRepository.findRecentGamesByTeamId(teamEntity.getId());
            for(MatchEntity matchEntity : matchEntityList){

                if(matchEntity.getWinner_id().toString().equals(teamEntity.getId().toString()) ) {
                    recentMatches.add("W");
                }
                else {
                    recentMatches.add("L");
                }
            }
            if(matchEntityList == null){
                log.info("getTeamRankList" + "최근 5경기 결과 불러오지 못함");
                return new ResponseEntity<>("최근 5경기 결과 불러오지 못함",HttpStatus.BAD_REQUEST);
            }

            //팀 id와 사진 저장
            teamRankingList.add(new TeamRanking(teamRankingEntity, teamEntity.getAcronym(), teamEntity.getImage_url(), recentMatches));
        }

        return new ResponseEntity<>(new TeamRankingList(teamRankingList), HttpStatus.OK);

    }

}
