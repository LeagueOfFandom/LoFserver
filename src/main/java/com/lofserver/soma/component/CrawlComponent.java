package com.lofserver.soma.component;

import com.lofserver.soma.dto.fcm.FcmResponse;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.entity.match.MatchInfo;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
    //모든 경기 설정 함수.
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setAllMatchList();
    }

    //매일 정각에 match 검색
    @Scheduled(cron = "1 0 0 * * *")
    public void monitoringLck(){
        LocalDate localDate = LocalDate.now();
        List<MatchEntity> matchEntityList = matchRepository.findByMatchDate(localDate);
        //오늘 매치가 있으면 아래 실행.
        if(matchEntityList != null){
        }
    }

    public void setAllMatchList(){//모든 매치 넣어주는 함수.
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
            String homeTeam = element.select("td[class^=matchlist-team1]").attr("data-teamhighlight");
            String awayTeam = element.select("td[class^=matchlist-team2]").attr("data-teamhighlight");
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

        return;
    }
    public void postAlarm(){

        String url = "https://fcm.googleapis.com/fcm/send";
        String key = "AAAA2e5oM2A:APA91bFkeAZM_08Vbliwn3C5_IR2jWF1GPgAS_9YYp071tRNyFossJP23OOTFMjwFq7HQW4HMU7K5XKee32u3cx8ioAlylFxK7SruyNO1iJy3sacuir-29GdosKdlKCBl6B_YfZj0xjd";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "key=" + key);
        JSONObject jsonObject = new JSONObject();
        JSONObject dataJson = new JSONObject();
        dataJson.put("title", "test");
        dataJson.put("message","gogo");
        jsonObject.put("to","fDE7uf6PTySGj3x9IWMo4b:APA91bFPwq7U7mYF-9FeHSBQlz9RCsZIbPl0PGheEvkqm-lkpXkrt9kO3MOIqOlJpvfvVlr-t73G4TpyuS2Zb24G52kw6EMK3cidtdOdhQJhlJI-P3ev6woGoOo657pEh7TC3RjudTVU");
        jsonObject.put("data", dataJson);
        log.info(jsonObject.toJSONString());

        HttpEntity<String> requestEntity = new HttpEntity<String>(jsonObject.toJSONString(),headers);

        ResponseEntity<FcmResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, FcmResponse.class);

    }
}
