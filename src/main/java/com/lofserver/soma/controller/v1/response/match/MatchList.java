package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.util.List;

@Getter
public class MatchList {
    private Match live;
    private List<Match> matchList;
    private Long totalPage;
    private Long curPage;

    public MatchList(Match live, List<Match> matchList, Long totalPage, Long curPage) {
        this.live = live;
        this.matchList = matchList;
        this.totalPage = totalPage;
        this.curPage = curPage;
    }
}
