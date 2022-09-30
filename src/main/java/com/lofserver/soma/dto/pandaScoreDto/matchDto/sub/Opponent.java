package com.lofserver.soma.dto.pandaScoreDto.matchDto.sub;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Opponent {
    private String acronym;
    private Long id;
    private String image_url;
    private String location;
    private String name;
    private LocalDateTime modified_at;
    private String slug;
}
