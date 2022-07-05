package com.lofserver.soma.service;

import com.lofserver.soma.dto.MatchDto;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.repository.MatchLckRepository;
import com.lofserver.soma.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DataService {

    private final MatchLckRepository matchLckRepository;
    private final TeamRepository teamRepository;

    private DataService(MatchLckRepository matchLckRepository, TeamRepository teamRepository) {
        this.matchLckRepository = matchLckRepository;
        this.teamRepository = teamRepository;
    }

    public void setMatch(List<MatchDto> match){
        match.forEach(matchDto -> {
            log.info(matchDto.toString());
            TeamEntity homeEntity = teamRepository.findById(matchDto.getHome_id()).orElse(null);
            TeamEntity awayEntity = teamRepository.findById(matchDto.getAway_id()).orElse(null);
            matchLckRepository.save(matchDto.toEntity(homeEntity,awayEntity));
        });
    }
}
