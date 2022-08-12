package com.lofserver.soma.dto.crawlDto.championDto;

import lombok.Getter;

@Getter
public class ChampionDto {
    private Long id;

    private String name;
    private String big_img_url;
    private String image_url;

    private Float armor;
    private Float armorperlevel;
    private Float attackdamage;
    private Float attackdamageperlevel;
    private Float attackrange;
    private Float attackspeedoffset;
    private Float attackspeedperlevel;
    private Float crit;
    private Float critperlevel;
    private Float hp;
    private Float hpperlevel;
    private Float hpregen;
    private Float hpregenperlevel;
    private Float movespeed;
    private Float mp;
    private Float mpperlevel;
    private Float mpregen;
    private Float mpregenperlevel;
    private Float spellblock;
    private Float spellblockperlevel;
}
