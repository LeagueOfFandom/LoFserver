package com.lofserver.soma.controller.v1.response.matchDetail;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TeamVsTeamSetInfo {
    private String viewType;
    private StringObject viewStringObject;
    private ImgObject viewImgObject;

    public TeamVsTeamSetInfo(String viewType, StringObject viewStringObject, ImgObject viewImgObject) {
        this.viewType = viewType;
        this.viewStringObject = viewStringObject;
        this.viewImgObject = viewImgObject;
    }
}
