package com.lofserver.soma.component;

import com.lofserver.soma.dto.fcm.FcmResponse;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.entity.match.MatchInfo;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CrawlComponent implements ApplicationRunner {

    String fandom_url = "https://lol.fandom.com/wiki/LCK/2022_Season/Summer_Season";
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private NextMatch nextMatch = new NextMatch();
    private String language = "ko_KR";
    //다음 경기에 대한 정보들

    //모든 경기 설정 함수.
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setAllMatchList();
        nextMatch.setMatchEntity(matchRepository.findFirstByHomeScoreAndAwayScore(0L,0L));
        nextMatch.setLocalTime(LocalTime.now(ZoneId.of("Asia/Seoul")));
    }
    //매일 정각에 match 검색
    @Scheduled(cron = "0 * * * * *")
    private void monitoringLck(){
        Document document = null;
        try {
            document = Jsoup.connect(fandom_url).get();
        } catch (IOException e) {
            log.info("fandom_url connection fail");
            return;
        }
        Elements elements = document.select("tr[class^=ml-allw ml-w]").select("tr[class*=ml-row]");
        elements.forEach(element -> {
            LocalDate matchDate = LocalDate.parse(element.attr("data-date"), DateTimeFormatter.ISO_DATE);
            LocalTime matchTime = LocalTime.parse(element.select("span[class^=ofl-toggle-3-5 ofl-toggler-3-all]").text());
            Elements scoreElements = element.select("td[class^=matchlist-score]");
            Long homeScore = 0L;
            Long awayScore = 0L;
            if (scoreElements.size() != 0) {
                homeScore = Long.parseLong(scoreElements.get(0).text());
                awayScore = Long.parseLong(scoreElements.get(1).text());
            }
            if(nextMatch.getMatchEntity().getMatchInfo().getMatchDate().equals(matchDate) && nextMatch.getMatchEntity().getMatchInfo().getMatchTime().equals(matchTime)){
                //live여부 확인.
                if(!nextMatch.getMatchEntity().getLive()){
                    LocalTime localTime = LocalTime.now(ZoneId.of("Asia/Seoul")).plusMinutes(10);
                    if(localTime.isAfter(nextMatch.getLocalTime())){
                        nextMatch.getMatchEntity().setLive(true);
                        post(nextMatch.getMatchEntity(),true);
                    }
                }
                //경기 변동 여부 확인.
                if(homeScore != nextMatch.getMatchEntity().getHomeScore() || awayScore != nextMatch.getMatchEntity().getAwayScore()){
                    nextMatch.getMatchEntity().setHomeScore(homeScore);
                    nextMatch.getMatchEntity().setAwayScore(awayScore);
                    //추후 db에 경기 수 넣어야할듯.
                    if(homeScore == 2L || awayScore == 2L){
                        nextMatch.getMatchEntity().setLive(false);
                        post(matchRepository.save(nextMatch.getMatchEntity()),false);
                        nextMatch.setMatchEntity(matchRepository.findFirstByHomeScoreAndAwayScore(0L,0L));
                        //다음경기가 오늘이라면
                        if(LocalDate.now(ZoneId.of("Asia/Seoul")).equals(nextMatch.getMatchEntity().getMatchInfo().getMatchDate())){
                            if(LocalTime.now(ZoneId.of("Asia/Seoul")).plusMinutes(50).isAfter(nextMatch.getMatchEntity().getMatchInfo().getMatchTime())) nextMatch.setLocalTime(LocalTime.now(ZoneId.of("Asia/Seoul")).plusMinutes(50));
                            else nextMatch.setLocalTime(nextMatch.getMatchEntity().getMatchInfo().getMatchTime());
                        }
                        else nextMatch.setLocalTime(nextMatch.getMatchEntity().getMatchInfo().getMatchTime());
                    }
                    else{
                        matchRepository.save(nextMatch.getMatchEntity());
                        post(nextMatch.getMatchEntity(), false);
                    }
                }
            }
        });
    }
    private void post(MatchEntity matchEntity, Boolean start){
        TeamEntity homeTeam = teamRepository.findById(matchEntity.getMatchInfo().getHomeTeamId()).orElse(null);
        TeamEntity awayTeam = teamRepository.findById(matchEntity.getMatchInfo().getAwayTeamId()).orElse(null);

        String url = "https://fcm.googleapis.com/fcm/send";
        String key = "AAAA2e5oM2A:APA91bFkeAZM_08Vbliwn3C5_IR2jWF1GPgAS_9YYp071tRNyFossJP23OOTFMjwFq7HQW4HMU7K5XKee32u3cx8ioAlylFxK7SruyNO1iJy3sacuir-29GdosKdlKCBl6B_YfZj0xjd";
        //header setting
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "key=" + key);
        //body setting
        JSONObject dataJson = new JSONObject();
        Long setCount = matchEntity.getAwayScore()+matchEntity.getHomeScore();
        dataJson.put("title", homeTeam.getTeamNameList().get(language) + " vs " + awayTeam.getTeamNameList().get(language));
        if(start) dataJson.put("message", "경기가 시작합니다!");
        else dataJson.put("message", setCount + "세트가 종료 되었습니다.");
        //user에게 fcm보내기
        List<UserEntity> userEntityList = userRepository.findAll();
        userEntityList.forEach(userEntity -> {
            //make json
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("to",userEntity.getToken());
            userJsonObject.put("data", dataJson);
            //user가 해당 경기를 선택했다면
            if(userEntity.getUserSelected().containsKey(matchEntity.getMatchId())){
                if(userEntity.getUserSelected().get(matchEntity.getMatchId())) userJsonObject.put("isShow",true);
                else userJsonObject.put("isShow",false);
            }
            else if(userEntity.getTeamList().contains(matchEntity.getMatchInfo().getAwayTeamId()) || userEntity.getTeamList().contains(matchEntity.getMatchInfo().getHomeTeamId())) userJsonObject.put("isShow",true);
            else userJsonObject.put("isShow",false);
            //send
            HttpEntity<String> requestEntity = new HttpEntity<String>(userJsonObject.toJSONString(),headers);
            ResponseEntity<FcmResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, FcmResponse.class);
            log.info(response.getBody().getSuccess().toString());
        });
    }

    private void setAllMatchList(){//모든 매치 넣어주는 함수.
        Document document = null;
        try {
            document = Jsoup.connect(fandom_url).get();
        } catch (IOException e) {
            log.info("fandom_url connection fail");
            return;
        }
        Elements elements = document.select("tr[class^=ml-allw ml-w]").select("tr[class*=ml-row]");
        elements.forEach(element -> {
            LocalDate matchDate = LocalDate.parse(element.attr("data-date"), DateTimeFormatter.ISO_DATE);
            LocalTime matchTime = LocalTime.parse(element.select("span[class^=ofl-toggle-3-5 ofl-toggler-3-all]").text());
            String homeTeam = element.select("td[class^=matchlist-team1]").select("span[class=teamname]").text();
            String awayTeam = element.select("td[class^=matchlist-team2]").select("span[class=teamname]").text();
            String liveLink = element.select("a[target=_blank]").attr("href");
            Elements scoreElements = element.select("td[class^=matchlist-score]");
            Long homeScore = 0L;
            Long awayScore = 0L;
            if(scoreElements.size() != 0) {
                homeScore = Long.parseLong(scoreElements.get(0).text());
                awayScore = Long.parseLong(scoreElements.get(1).text());
            }

            TeamEntity teamEntityHome = teamRepository.findByTeamName(homeTeam);
            TeamEntity teamEntityAway = teamRepository.findByTeamName(awayTeam);

            Long matchId = matchRepository.save(new MatchEntity(homeScore,awayScore,false,new MatchInfo(matchDate,matchTime,teamEntityHome.getTeamId(), teamEntityAway.getTeamId(),liveLink))).getMatchId();
            teamEntityHome.addMatch(matchId);
            teamEntityAway.addMatch(matchId);
            teamRepository.save(teamEntityHome);
            teamRepository.save(teamEntityAway);
        });
    }
}
