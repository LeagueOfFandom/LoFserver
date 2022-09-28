package com.lofserver.soma.controller.v1.response.match;

import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.match.sub.DateInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MatchList {
    private List<DateInfo> dateList;
    private List<List<CommonItem>> matchDataList;

    @Builder
    public MatchList(List<DateInfo> dateList, List<List<CommonItem>> matchDataList) {
        this.dateList = dateList;
        this.matchDataList = matchDataList;
    }
}
