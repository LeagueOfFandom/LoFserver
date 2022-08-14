package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

@Getter
public class Roster {
    private Long id;
    private String position;
    private String name;
    private String positionImg;
    private String playerImg;

    public Roster(Long id, String position, String name, String positionImg, String playerImg) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.positionImg = positionImg;
        this.playerImg = playerImg;
    }
}
