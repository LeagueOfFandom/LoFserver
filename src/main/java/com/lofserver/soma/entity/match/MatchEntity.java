package com.lofserver.soma.entity.match;


import com.lofserver.soma.controller.v1.response.match.Match;
import com.lofserver.soma.controller.v1.response.match.MatchDetails;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.repository.TeamRepository;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "match_lck")
@TypeDef(name = "json", typeClass = JsonStringType.class)
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
    public MatchDetails toMatchDetails(Boolean alarm, TeamRepository teamRepository){
        TeamEntity teamEntityHome = teamRepository.findById(matchInfo.getHomeTeamId()).orElse(null);
        TeamEntity teamEntityAway = teamRepository.findById(matchInfo.getAwayTeamId()).orElse(null);
        return new MatchDetails(matchId, matchInfo.getMatchDate(), matchInfo.getMatchTime(),teamEntityHome.getTeamName(),teamEntityAway.getTeamName(),teamEntityHome.getTeamImg(),teamEntityAway.getTeamImg(),homeScore,awayScore,matchInfo.getLiveLink(),alarm);
    }
    public void setHomeScore(Long homeScore) {
        this.homeScore = homeScore;
    }
    public void setAwayScore(Long awayScore) {
        this.awayScore = awayScore;
    }
    public void setLive(Boolean live) {
        this.live = live;
    }

    public MatchEntity(Long homeScore, Long awayScore, Boolean live, MatchInfo matchInfo) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.live = live;
        this.matchInfo = matchInfo;
    }
}
