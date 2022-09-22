package com.lofserver.soma.controller.v1.response.match;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DateInfo {
    private String month;
    private String date;
    private String color;

    public DateInfo(LocalDate date) {

        this.month = date.getMonth().getValue() + "월";
        this.date = date.getDayOfMonth() + "일";

        if(date.getDayOfWeek().getValue() == 6){
            this.color = "#426BFF";
        } else if (date.getDayOfWeek().getValue() == 7){
            this.color = "#E2012D";
        } else {
            this.color = "#000000";
        }
    }
}
