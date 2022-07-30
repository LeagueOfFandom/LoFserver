package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Match {
    private LocalDate date;
    private List<MatchDetails> match;

    public Match(LocalDate date, List<MatchDetails> match) {
        this.date = date;
        this.match = match;
    }
}
