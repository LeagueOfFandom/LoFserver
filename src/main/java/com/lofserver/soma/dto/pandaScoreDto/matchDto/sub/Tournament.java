package com.lofserver.soma.dto.pandaScoreDto.matchDto.sub;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Tournament {
    private LocalDateTime begin_at;
    private LocalDateTime end_at;
    private String name;
    private Long id;
    private Long league_id;
    private Boolean live_supported;
    private LocalDateTime modified_at;
    private String prizepool;
    private Long serie_id;
    private String slug;
    private String tier;
    private Long winner_id;
    private String winner_type;
}
