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

    @Query(value = "select * from match_lck where(json_contains(match_info,json_array(?3,?4,?5),'$.matchDate') and ((json_contains(match_info,?1,'$.homeTeamId') and json_contains(match_info,?2,'$.awayTeamId')) or (json_contains(match_info,?2,'$.homeTeamId') and json_contains(match_info,?1,'$.awayTeamId'))))",nativeQuery = true)
    List<MatchEntity> findByTeamIdsAndMatchDate(Long homeTeamId, Long awayTeamId, int year, int month, int day);
    MatchEntity findFirstByHomeScoreAndAwayScore(Long homeScore, Long awayScore);

    @Query(value = "SELECT * FROM match_lck WHERE(json_value(match_info,'$.homeTeamId') = ?1 and (home_score = 2 or away_score=2)) or (json_value(match_info,'$.awayTeamId') = ?1 and (home_score = 2 or away_score=2)) ORDER BY id DESC limit 5;" ,nativeQuery = true)
    List<MatchEntity> findByTeamId(Long teamId);
}
