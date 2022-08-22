package com.lofserver.soma.dto.crawlDto.matchDto.sub;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Game {
    private LocalDateTime begin_at;
    private LocalDateTime end_at;
    private Long id;
    private Long length;
    private Long match_id;
    private Winner winner;
    private String status;
}
