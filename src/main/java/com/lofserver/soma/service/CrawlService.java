package com.lofserver.soma.service;

import com.lofserver.soma.entity.match.MatchEntity;
import com.lofserver.soma.entity.match.MatchInfo;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import com.lofserver.soma.repository.UserRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import java.io.IOException;

@Service
public class CrawlService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public CrawlService(UserRepository userRepository, TeamRepository teamRepository, MatchRepository matchRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

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
            }
        }
        return;
    }
}
