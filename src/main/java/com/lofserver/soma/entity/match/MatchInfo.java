package com.lofserver.soma.entity.match;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class MatchInfo {
    private LocalDate matchDate;
    private LocalTime matchTime;
    private String homeName;
    private String awayName;
    private String homeImg;
    private String awayImg;
}
