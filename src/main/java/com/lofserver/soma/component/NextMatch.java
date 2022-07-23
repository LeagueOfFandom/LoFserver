package com.lofserver.soma.component;

import com.lofserver.soma.entity.match.MatchEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class NextMatch {
    private MatchEntity matchEntity;
    private LocalTime localTime;
}
