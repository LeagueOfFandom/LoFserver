package com.lofserver.soma.dto.pandaScoreDto.leagueDto;

import com.lofserver.soma.dto.pandaScoreDto.leagueDto.sub.Series;
import com.lofserver.soma.dto.pandaScoreDto.leagueDto.sub.VideoGame;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class LeagueListDto {
    private Long id;
    private String image_url;
    private LocalDateTime modified_at;
    private String name;
    private List<Series> series;
    private String slug;
    private String url;
    private VideoGame videogame;
}
