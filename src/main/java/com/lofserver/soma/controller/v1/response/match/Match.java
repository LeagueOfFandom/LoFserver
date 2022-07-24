package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Match {
    private LocalDate date;
    private MatchDetails match;

    public Match(LocalDate date, MatchDetails match) {
        this.date = date;
        this.match = match;
    }
}
