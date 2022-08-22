package com.lofserver.soma.dto.crawlDto.gameDto.sub.teams;


import com.lofserver.soma.dto.crawlDto.matchDto.sub.Opponent;
import lombok.Getter;

import java.util.List;

@Getter
public class Team {
    private List<Long> bans;
    private Long herald_kills;
    private Long gold_earned;
    private Long baron_kills;
    private Long chemtech_drake_kills;
    private Long cloud_drake_kills;
    private Long elder_drake_kills;
    private Long dragon_kills;
    private Long hextech_drake_kills;
    private Long infernal_drake_kills;
    private Long inhibitor_kills;
    private Long mountain_drake_kills;
    private Long ocean_drake_kills;
    private String color;
    private List<Long> player_ids;
    private Long tower_kills;
    private Opponent team;
}
