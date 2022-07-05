package com.lofserver.soma.dto;


import com.lofserver.soma.entity.MatchLckEntity;
import com.lofserver.soma.entity.TeamEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@ToString
public class MatchDto {

    private Long match_id;
    private Long home_id;
    private Long away_id;
    private LocalDate match_date;
    private LocalTime match_time;
    private Long home_score;
    private Long away_score;

    public MatchLckEntity toEntity(TeamEntity homeEntity, TeamEntity awayEntity){
        return new MatchLckEntity(null,homeEntity, awayEntity, this.match_date, this.match_time,this.home_score,this.away_score);
    }
}
