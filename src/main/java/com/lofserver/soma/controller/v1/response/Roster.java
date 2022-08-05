package com.lofserver.soma.controller.v1.response;

import lombok.Getter;

@Getter
public class Roster {
    private String position;
    private String name;
    private String positionImg;

    public Roster(String position, String name, String positionImg) {
        this.position = position;
        this.name = name;
        this.positionImg = positionImg;
    }
}
