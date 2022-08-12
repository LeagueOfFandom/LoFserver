package com.lofserver.soma.entity;

import com.lofserver.soma.controller.v1.response.match.MatchDetails;
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
    private Long league_id;

    @Column(name = "original_schedule_at")
    private LocalDateTime original_scheduled_at;

    @Column(name = "begin_at")
    private LocalDateTime begin_at;

    @Column(name = "winner_id")
    private Long winner_id;

    @Column(name = "status")
    private String status;

    @Column(name = "end_at")
    private LocalDateTime end_at;

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
    private List<Stream> streams_list;

    @Type(type = "json")
    @Column(name = "opponents", columnDefinition = "json")
    private List<Opponents> opponents;

    public MatchEntity(Long id, Tournament tournament, Long league_id, LocalDateTime original_scheduled_at, LocalDateTime begin_at, Long winner_id, String status, LocalDateTime end_at, Live live, List<Result> results, List<Game> games, List<Stream> streams_list, List<Opponents> opponents) {
        this.id = id;
        this.tournament = tournament;
        this.league_id = league_id;
        this.original_scheduled_at = original_scheduled_at;
        this.begin_at = begin_at;
        this.winner_id = winner_id;
        this.status = status;
        this.end_at = end_at;
        this.live = live;
        this.results = results;
        this.games = games;
        this.streams_list = streams_list;
        this.opponents = opponents;
    }

    public MatchDetails toMatchDetails(Boolean alarm) {
        String homeTeam = "미정";
        String awayTeam = "미정";
        String homeUrl = "미정";
        String awayUrl = "미정";
        Long homeScore = 0L;
        Long awayScore = 0L;
        if(opponents.size() != 0) {
            homeTeam = opponents.get(0).getOpponent().getName();
            awayTeam = opponents.get(1).getOpponent().getName();
            homeUrl = opponents.get(0).getOpponent().getImage_url();
            awayUrl = opponents.get(1).getOpponent().getImage_url();
            homeScore = results.get(0).getScore();
            awayScore = results.get(1).getScore();
        }
        return new MatchDetails(
                id,
                original_scheduled_at.toLocalDate(),
                original_scheduled_at.toLocalTime(),
                homeTeam,
                awayTeam,
                homeUrl,
                awayUrl,
                homeScore,
                awayScore,
                "https://www.twitch.tv/lck?lang=ko",
                alarm);
    }
}
