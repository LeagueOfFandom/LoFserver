package com.lofserver.soma.dto.crawlDto.matchDto.sub;

import lombok.Getter;

@Getter
public class Opponent {
    private String acronym;
    private Long id;
    private String image_url;
    private String location;
    private String name;

    public Opponent() {
        acronym = "미정";
        image_url = "";
    }
}
