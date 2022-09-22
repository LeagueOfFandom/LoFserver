package com.lofserver.soma.controller.v1.response.match;

import com.lofserver.soma.controller.v1.response.CommonItem;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Match {
    private List<DateInfo> dateList;
    private List<List<CommonItem>> matchDataList;
    public Match(List<DateInfo> dateList, List<List<CommonItem>> matchDataList) {
        this.dateList = dateList;
        this.matchDataList = matchDataList;
    }
}
