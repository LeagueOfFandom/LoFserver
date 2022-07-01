package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchList {
    private List<Match> matchList;

    public MatchList(List<Match> matchList) {
        this.matchList = matchList;
    }
}
