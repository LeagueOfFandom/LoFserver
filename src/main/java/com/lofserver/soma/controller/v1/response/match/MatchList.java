package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchList {
    private Match live;
    private List<Match> matchList;
    public MatchList(Match live, List<Match> matchList) {
        this.live = live;
        this.matchList = matchList;
    }
}
