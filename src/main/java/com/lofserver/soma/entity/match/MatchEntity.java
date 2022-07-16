package com.lofserver.soma.entity.match;


import com.lofserver.soma.controller.v1.response.match.Match;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "match_lck")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long matchId; //임의로 생성된 match id.
    @Column(name = "home_score")
    private Long homeScore; //home team의 score
    @Column(name = "away_score")
    private Long awayScore; //away team의 score
    @Column(name = "live")
    private Boolean live; //해당 match의 live 여부.
    @Type(type = "json")
    @Column(name = "match_info", columnDefinition = "json")
    private MatchInfo matchInfo; //match 에 대한 상세 정보. 추후 vs 정보등 추가.

    //client로 보내줄 match 형태로 link와 alarm을 추가하여 변경.
    public Match toMatch(String livelink, Boolean alarm){
        return new Match(matchId, matchInfo.getMatchDate(), matchInfo.getMatchTime(),matchInfo.getHomeName(),matchInfo.getAwayName(),matchInfo.getHomeImg(),matchInfo.getAwayImg(),homeScore,awayScore,live,livelink,alarm);
    }

    public MatchEntity(MatchInfo matchinfo, Long homeScore, Long awayScore, Boolean live){
        this.matchInfo = matchinfo;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.live = live;
    }
}
