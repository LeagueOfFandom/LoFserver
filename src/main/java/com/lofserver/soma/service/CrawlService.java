package com.lofserver.soma.service;

import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.entity.match.MatchInfo;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class CrawlService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public CrawlService(UserRepository userRepository, TeamRepository teamRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    /*
    public void getTeamId(String teamName){
        System.out.println("getid" + matchRepository.count());

        teamRepository.findAll().forEach(mem->{
            log.info(mem.toString());
        });
    }
    */

    public void getMatchScheduleList() { // 서비스에서 호출할 메소드
        final String matchInfo = "https://lol.fandom.com/wiki/LCK/2022_Season/Summer_Season";
        Connection conn = Jsoup.connect(matchInfo); // 커넥션 생성
        try {
            Document document = conn.get(); // document 객체 생성
            getMatchScheduleList(document);
        } catch (IOException ignored) {
        }
        return;
    }

    public void getMatchScheduleList(Document document) { // 크롤링 값들을 리스트로 반환
        Elements elements = document.select("#matchlist-content-wrapper tbody tr");

        String DateInLocal = "";

        for (Element element : elements) {

            if(!element.select("td span.DateInLocal").isEmpty()) {
                DateInLocal = element.text();
            }

            if(element.select("td").size() == 5){
                String homeName="";
                String awayName="";
                long homeScore = 0;
                long awayScore = 0;

                for(int i=0; i<(element.select("td").size()); i++){
                    if (i==0) {
                        homeName = element.select("td").get(i).text();
                    }
                    else if (i==1) {
                        homeScore = Long.parseLong(element.select("td").get(i).text());
                    }
                    else if(i==2) {
                        awayScore= Long.parseLong(element.select("td").get(i).text());
                    }
                    else if(i==3) continue;
                    else if(i==4) {
                        awayName= element.select("td").get(i).text();
                    }
                }

                String Date = DateInLocal.substring(0,9);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy,M,dd");
                LocalDate matchDate = LocalDate.parse(Date, formatter);

                String Time = DateInLocal.substring(10);
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH,mm");
                LocalTime matchTime = LocalTime.parse(Time, formatter2);
                matchTime =  matchTime.plusHours(9);

                matchRepository.save(new MatchEntity(new MatchInfo(matchDate, matchTime, homeName, awayName), homeScore, awayScore, false));
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

                String Date = "";

                if(DateTime.indexOf("July")!=-1){
                    String[] Days = DateTime.split(" ");
                    String Day = Days[0];
                    if(Day.length() == 1) Day = "0".concat(Day);
                    String Month = "07";
                    String Year = "2022";
                    Date = Year + Month + Day;
                }
                else if(DateTime.indexOf("August")!=-1){
                    String[] Days = DateTime.split(" ");
                    String Day = Days[0];
                    if(Day.length() == 1) Day = "0".concat(Day);
                    String Month = "08";
                    String Year = "2022";
                    Date = Year + Month + Day;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate matchDate = LocalDate.parse(Date, formatter);

                String Time = DateTime.substring(DateTime.length()-5, DateTime.length());
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime matchTime = LocalTime.parse(Time, formatter2);

                matchRepository.save(new MatchEntity(new MatchInfo(matchDate, matchTime, homeName, awayName), homeScore, awayScore, false));
                matchRepository.flush();
            }
        }
        return;
    }
}
