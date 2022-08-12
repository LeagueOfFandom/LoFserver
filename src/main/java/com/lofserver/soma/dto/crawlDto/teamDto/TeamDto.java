package com.lofserver.soma.dto.crawlDto.teamDto;


import com.lofserver.soma.dto.crawlDto.gameDto.sub.player.PlayerDetails;
import com.lofserver.soma.entity.TeamEntity;
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

    public TeamEntity toTeamEntity(){
        return new TeamEntity(id, acronym, image_url, location, name, players);
    }
}
