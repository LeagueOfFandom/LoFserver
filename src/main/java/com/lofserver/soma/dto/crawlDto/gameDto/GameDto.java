package com.lofserver.soma.dto.crawlDto.gameDto;

import com.lofserver.soma.dto.crawlDto.gameDto.sub.player.Player;
import com.lofserver.soma.dto.crawlDto.gameDto.sub.teams.Team;
import com.lofserver.soma.dto.crawlDto.matchDto.sub.Winner;
import com.lofserver.soma.entity.MatchDetailEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GameDto {
    private LocalDateTime begin_at;
    private LocalDateTime end_at;
    private Long id;
    private Long length;
    private List<Player> players;
    private String status;
    private Winner winner;
    private List<Team> teams;

    public MatchDetailEntity toMatchDetailEntity(){
        return new MatchDetailEntity(id, begin_at, end_at, status, length, players, winner, teams);
    }

}
