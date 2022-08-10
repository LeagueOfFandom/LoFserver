package com.lofserver.soma.controller.v1;

import com.lofserver.soma.entity.match.MatchDetailSet;
import com.lofserver.soma.entity.match.MatchDetailsEntity;
import com.lofserver.soma.repository.MatchDetailsRepository;
import com.lofserver.soma.repository.MatchRepository;
import com.lofserver.soma.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DataSetController {

    private final MatchDetailsRepository matchDetailsRepository;
    private final MatchRepository matchRepository;
    @PostMapping("/dataset")
    public String dataset(@RequestParam(value = "homeTeamid")Long homeTeamId, @RequestParam(value = "awayTeamid")Long awayTeamId, @RequestParam(value = "date") String localDate1, @RequestBody List<MatchDetailSet> matchDetailSetList) {
        LocalDate localDate = LocalDate.parse(localDate1);
        matchDetailsRepository.save(new MatchDetailsEntity(matchRepository.findByTeamIdsAndMatchDate(homeTeamId, awayTeamId, localDate.getYear(),localDate.getMonthValue(),localDate.getDayOfMonth()).getMatchId(),matchDetailSetList));
        return "success";
    }
}
