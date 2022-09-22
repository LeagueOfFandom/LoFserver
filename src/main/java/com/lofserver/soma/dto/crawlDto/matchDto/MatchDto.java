package com.lofserver.soma.dto.crawlDto.matchDto;


import com.lofserver.soma.dto.crawlDto.matchDto.sub.*;
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


}
