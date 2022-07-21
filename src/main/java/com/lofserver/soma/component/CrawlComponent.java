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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class CrawlComponent implements ApplicationRunner {

    String fandom_url = "https://lol.fandom.com/wiki/LCK/2022_Season/Summer_Season";
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    //모든 경기 설정 함수.
    @Override
    public void run(ApplicationArguments args) throws Exception{
        setAllMatchList();
        monitoringLck();
    }

    /*
    @Scheduled(cron = "0 0 0 * * 4") // 수요일날 실행
    public void monitoringWeek(){
        monitoringLck(6L);
    }
    */

    @Scheduled(cron = "0 0/5 17-23 * * *") // @Scheduled(cron = "10 * * * * *")
    public void monitoringLck(){
        LocalDate localDate = LocalDate.now();

        Long Week = 6L;
        Long Order = 3L;

        // 매일의 matchid를 list에 저장
        // List<MatchEntity> matchEntityList = matchRepository.findByMatchDate(localDate);
        List<MatchEntity> matchEntityList = new ArrayList<>();
        matchRepository.findAll().forEach(match -> {
            if(match.getMatchInfo().getMatchDate().toString().equals(localDate.toString())){
                matchEntityList.add(match);
            }
        });

        // 오늘 매치가 있으면 실행
        if(matchEntityList.size()!=0){
            log.info("오늘 경기가 있다면");
            // checkTodayList(localDate.toString(), matchEntityList.get(0).getMatchId());
           checkTodayList(Week, Order, matchEntityList.get(0).getMatchId());
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

    public void checkTodayList(Long Week, Long Order, Long matchId) {
        MatchEntity matchEntity = matchRepository.findById(matchId).get();

        Document document = null;
        try {
            document = Jsoup.connect(fandom_url).get();
        } catch (IOException e) {
            log.info("fandom_url connection fail");
            return;
        }

        Elements elements = document.select("tr[class^=ml-allw ml-w"+Week.toString()+"]").select("tr[class*=ml-row]");
        elements.forEach(element -> {
            log.info(element.attr("data-initial-order")+ " " + Order.toString());
            if(element.attr("data-initial-order").equals(Order.toString())){

                // 시간 확인
                LocalTime localTime = LocalTime.now();
                if (!matchEntity.getLive()){
                    if (localTime.isAfter(matchEntity.getMatchInfo().getMatchTime())) { // 경기 시간 지났으면
                        matchEntity.setLive(true);
                        matchRepository.save(matchEntity);
                        matchRepository.flush();
                        log.info("경기시작 후 " + matchRepository.findById(matchId).get().getLive());
                    }
                    else {
                        log.info("경기시작 전");
                    }
                }

                // 점수 확인
                Long homeScore_ori =  matchEntity.getHomeScore();
                Long awayScore_ori =  matchEntity.getAwayScore();
                Elements scoreElements = element.select("td[class^=matchlist-score]");
                Long homeScore = homeScore_ori;
                Long awayScore = awayScore_ori;
                if(scoreElements.size() != 0) {
                    homeScore = Long.parseLong(scoreElements.get(0).text());
                    awayScore = Long.parseLong(scoreElements.get(1).text());
                }
                log.info("이전 점수 home: " + homeScore_ori.toString() + " away: " + awayScore_ori.toString());
                log.info("이후 점수 home: " + homeScore.toString() + " away: " + awayScore.toString());

                // 점수가 업데이트 되었다면
                if (homeScore_ori != homeScore || awayScore_ori != awayScore) {
                    matchEntity.setHomeScore(homeScore);
                    matchEntity.setAwayScore(awayScore);
                    matchRepository.save(matchEntity);
                    matchRepository.flush();
                    log.info("라운드 종료 repo " + matchRepository.findById(matchId).get().getHomeScore() + " " +  matchRepository.findById(matchId).get().getAwayScore());
                    log.info("라운드 종료 entity" + matchRepository.findById(matchId).get().getHomeScore() + " " +  matchRepository.findById(matchId).get().getAwayScore());

                    if (homeScore == 2 || awayScore == 2) { // 게임 종료 & 라이브 변경
                        matchEntity.setLive(false);
                        matchRepository.save(matchEntity);
                        matchRepository.flush();
                        log.info("경기 종료 라이브변경" + matchRepository.findById(matchId).get().getLive());

                        checkTodayList(Week, Order+1, matchId+1);
                        return;
                    }
                    // fcm push
                }

            }
        });
    }

}