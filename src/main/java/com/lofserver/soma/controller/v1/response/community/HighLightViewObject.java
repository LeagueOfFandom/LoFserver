package com.lofserver.soma.controller.v1.response.community;

import lombok.Getter;

import java.util.List;

@Getter
public class HighLightViewObject {
    private List<String> videoList;

    public HighLightViewObject(List<String> videoList) {
        this.videoList = videoList;
    }
}
