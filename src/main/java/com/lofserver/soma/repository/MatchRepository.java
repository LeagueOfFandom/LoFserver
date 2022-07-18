package com.lofserver.soma.repository;

import com.lofserver.soma.entity.match.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("SELECT ME.matchId from MatchEntity ME")
    List<Long> findAllId();
    @Query(value = "select * from match_lck where json_contains(match_info,?1,'$.matchDate')",nativeQuery = true)
    List<MatchEntity> findByMatchDate(LocalDate localDate);
}
