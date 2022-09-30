package com.lofserver.soma.dto.pandaScoreDto.matchDto;

import com.lofserver.soma.dto.pandaScoreDto.matchDto.sub.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MatchDto {

    private Boolean rescheduled;
    private String winner;
    private String winner_type;
    private LocalDateTime scheduled_at;
    private List<Stream> streams_list;
    private Boolean forfeit;
    private String official_stream_url;
    private Long id;
    private Long number_of_games;
    private List<Opponents> opponents;
    private String videogame_version;
    private Long tournament_id;
    private String name;
    private League league;
    private Tournament tournament;
    private LocalDateTime begin_at;
    private String slug;
    private String status;
    private String match_type;
    private String live_embed_url;
    private Long winner_id;
    private Serie serie;
    private Boolean detailed_stats;
    private List<Result> results;
    private Boolean draw;
    private Live live;
    private LocalDateTime end_at;
    private LocalDateTime modified_at;
    private Long serie_id;
    private LocalDateTime original_scheduled_at;
    private List<Game> games;
    private Long league_id;

}
