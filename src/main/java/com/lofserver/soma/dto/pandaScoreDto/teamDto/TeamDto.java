package com.lofserver.soma.dto.pandaScoreDto.teamDto;

import com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.player.PlayerDetails;
import lombok.Getter;

import java.util.List;

@Getter
public class TeamDto {
    private String acronym;
    private Long id;
    private String image_url;
    private String location;
    private String name;
    private List<PlayerDetails> players;

}
