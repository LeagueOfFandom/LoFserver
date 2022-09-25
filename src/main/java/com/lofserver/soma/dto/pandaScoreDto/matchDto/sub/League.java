package com.lofserver.soma.dto.pandaScoreDto.matchDto.sub;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class League {
    private Long id;
    private String image_url;
    private LocalDateTime modified_at;
    private String name;
    private String slug;
    private String url;
}
