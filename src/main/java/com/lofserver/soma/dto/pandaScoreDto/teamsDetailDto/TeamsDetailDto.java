package com.lofserver.soma.dto.pandaScoreDto.teamsDetailDto;

import com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.player.PlayerDetails;
import com.lofserver.soma.dto.pandaScoreDto.teamsDetailDto.sub.Status;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamsDetailDto {
    private String acronym;
    private Long id;
    private String image_url;
    private String name;
    private List<PlayerDetails> players;
    private Status status;
}
