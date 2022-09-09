package com.lofserver.soma.dto.leagueDto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Series {
    private LocalDateTime begin_at;
    private String description;
    private LocalDateTime end_at;
    private String full_name;
    private Long id;
    private Long league_id;
    private LocalDateTime modified_at;
    private String name;
    private String season;
    private String slug;
    private String tier;
    private Long winner_id;
    private String winner_type;
    private Long year;
}
