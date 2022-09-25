package com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.player;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlayerDetails {
    private Long age;
    private Long birth_year;
    private LocalDate birthday;
    private String first_name;
    private String hometown;
    private Long id;
    private String image_url;
    private String last_name;
    private String name;
    private String role;
}
