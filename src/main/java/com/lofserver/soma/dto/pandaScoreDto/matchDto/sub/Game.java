package com.lofserver.soma.dto.pandaScoreDto.matchDto.sub;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Game {
    private LocalDateTime begin_at;
    private Boolean complete;
    private Boolean detailed_stats;
    private LocalDateTime end_at;
    private Boolean finished;
    private Boolean forfeit;
    private Long id;
    private Long length;
    private Long match_id;
    private Long position;
    private Winner winner;
    private String status;
    private String winner_type;
}
