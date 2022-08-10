package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamRankDto {

    private String year;
    private String season;
    private String league;

    public TeamRankDto(String year, String season, String league){
        this.year = year;
        this.season = season;
        this.league = league;
    }
}
