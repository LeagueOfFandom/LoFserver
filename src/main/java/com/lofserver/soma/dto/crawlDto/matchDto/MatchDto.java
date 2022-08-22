package com.lofserver.soma.dto.crawlDto.matchDto;


import com.lofserver.soma.dto.crawlDto.matchDto.sub.*;
import com.lofserver.soma.entity.MatchEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MatchDto {
    private Tournament tournament;
    private Long league_id;
    private LocalDateTime original_schedule_at;
    private Live live;
    private LocalDateTime end_at;
    private List<Result> results;
    private List<Game> games;
    private Long id;
    private List<Stream> streams_list;
    private LocalDateTime begin_at;
    private Long winner_id;
    private String status;
    private List<Opponents> opponents;

    public MatchEntity toEntity(){
        return new MatchEntity(
                id,
                tournament,
                league_id,
                original_schedule_at,
                begin_at,
                winner_id,
                status,
                end_at,
                live,
                results,
                games,
                streams_list,
                opponents);
    }

}
