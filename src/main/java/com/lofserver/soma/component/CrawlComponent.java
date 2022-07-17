package com.lofserver.soma.component;

import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.entity.match.MatchInfo;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Slf4j
public class CrawlComponent implements ApplicationRunner {

    String fandom_url = "https://lol.fandom.com/wiki/LCK/2022_Season/Summer_Season";
    private final MatchRepository matchRepository;

    private final TeamRepository teamRepository;

    public CrawlComponent(MatchRepository matchRepository, TeamRepository teamRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    //모든 경기 설정 함수.
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setAllMatchList();
    }
    public Boolean setAllMatchList(){
        Document document = null;
        try {
            document = Jsoup.connect(fandom_url).get();
        } catch (IOException e) {
            log.info("fandom_url connection fail");
            return false;
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

        return true;
    }
/*
    public void getMatchScheduleList(Document document) { // 크롤링 값들을 리스트로 반환
        Elements elements = document.select("#matchlist-content-wrapper tbody tr");

        String DateInLocal = "";

        for (Element element : elements) {

            if(!element.select("td span.DateInLocal").isEmpty()) {
                DateInLocal = element.text();
                System.out.println(DateInLocal);
            }

            if(element.select("td").size() == 5){
                String homeName="";
                String awayName="";
                long homeScore = 0;
                long awayScore = 0;

                for(int i=0; i<(element.select("td").size()); i++){
                    if (i==0) {
                        homeName = element.select("td").get(i).text();
                        System.out.println("home" + homeName);
                    }
                    else if (i==1) {
                        homeScore = Long.parseLong(element.select("td").get(i).text());
                        System.out.println(homeScore);
                    }
                    else if(i==2) {
                        awayScore= Long.parseLong(element.select("td").get(i).text());
                        System.out.println(awayScore);
                    }
                    else if(i==3) continue;
                    else if(i==4) {
                        awayName= element.select("td").get(i).text();
                        System.out.println("away" + awayName);
                    }
                }
                System.out.println(awayScore);
                matchRepository.save(new MatchEntity(homeScore, awayScore));
                matchRepository.flush();
            }

            if(element.select("td").size() == 3){
                String DateTime = "";
                String homeName="";
                String awayName="";
                long homeScore = 0;
                long awayScore = 0;

                for(int i=0; i<(element.select("td").size()); i++){
                    if (i==0) {
                        homeName = element.select("td").get(i).text();
                    }
                    else if (i==1) {
                        DateTime = element.select("td").get(i).text();
                    }
                    else if(i==2) {
                        awayName= element.select("td").get(i).text();
                    }
                }
                matchRepository.save(new MatchEntity(homeScore, awayScore), false);
                matchRepository.flush();
            }
        }
        return;
    }*/
}
