package com.lofserver.soma.entity;

import com.lofserver.soma.controller.v1.response.match.MatchViewObject;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.*;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "match_list")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MatchEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Type(type = "json")
    @Column(name = "tournament", columnDefinition = "json")
    private Tournament tournament;

    @Column(name = "league_id")
    private Long leagueId;

    @Column(name = "original_schedule_at")
    private LocalDateTime originalScheduledAt;

    @Column(name = "begin_at")
    private LocalDateTime beginAt;

    @Column(name = "winner_id")
    private Long winnerId;

    @Column(name = "status")
    private String status;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Type(type = "json")
    @Column(name = "live", columnDefinition = "json")
    private Live live;

    @Type(type = "json")
    @Column(name = "results", columnDefinition = "json")
    private List<Result> results;

    @Type(type = "json")
    @Column(name = "games", columnDefinition = "json")
    private List<Game> games;

    @Type(type = "json")
    @Column(name = "streams_list", columnDefinition = "json")
    private List<Stream> streamsList;

    @Type(type = "json")
    @Column(name = "opponents", columnDefinition = "json")
    private List<Opponents> opponents;

    public MatchViewObject toMatchViewObject(Boolean isAlarm) {

        Opponent homeTeam = new Opponent();
        Opponent awayTeam = new Opponent();
        Long homeScore = 0L;
        Long awayScore = 0L;

        if(opponents != null && opponents.size() != 0) {
            homeTeam = opponents.get(0).getOpponent();
            awayTeam = opponents.get(1).getOpponent();

            homeScore = results.get(0).getScore();
            awayScore = results.get(1).getScore();
            if (results.get(1).getTeam_id() == homeTeam.getId()) {
                homeScore = results.get(1).getScore();
                awayScore = results.get(0).getScore();
            }
        }

        return MatchViewObject.builder()
                .matchId(id)
                .homeName(homeTeam.getAcronym())
                .homeImg(homeTeam.getImage_url())
                .awayName(awayTeam.getAcronym())
                .awayImg(awayTeam.getImage_url())
                .time(beginAt.toString())
                .league(tournament.getName())
                .isAlarm(isAlarm)
                .homeScore(homeScore)
                .awayScore(awayScore)
                .build();

    }
}
