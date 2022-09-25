package com.lofserver.soma.dto.pandaScoreDto.gameDto;

import com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.player.Player;
import com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.teams.Team;
import com.lofserver.soma.dto.pandaScoreDto.matchDto.sub.Winner;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GameDto {
    private LocalDateTime begin_at;
    private Boolean complete;
    private Boolean detailed_stats;
    private LocalDateTime end_at;
    private Boolean finished;
    private Boolean forfeit;
    private Long id;
    private Long length;
    private List<Player> players;
    private String status;
    private Winner winner;
    private List<Team> teams;
    private Long position;

}
