package com.lofserver.soma.repository;

import com.lofserver.soma.entity.match.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("SELECT ME.matchId from MatchEntity ME")
    List<Long> findAllId();
    @Query(value = "select * from match_lck where json_contains(match_info,?1,'$.homeTeamId') and json_contains(match_info,?2,'$.awayTeamId')",nativeQuery = true)
    List<MatchEntity> findByHomeTeamIdAndAwayTeamId(Long homeTeamId, Long awayTeamId);
    MatchEntity findFirstByHomeScoreAndAwayScore(Long homeScore, Long awayScore);
}
