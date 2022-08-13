package com.lofserver.soma.repository;


import com.lofserver.soma.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("select m.id from MatchEntity m")
    List<Long> findAllId();

    @Query(value = "select * from match_list where original_schedule_at >= ?1 and league_id = ?4 and (json_value(opponents,'$[0].opponent.id') in ?5 or json_value(opponents,'$[1].opponent.id') in ?5) order by original_schedule_at desc limit ?2, ?3", nativeQuery = true)
    List<MatchEntity> findAllAfterMatchByTeamIds(LocalDateTime dateTime, int start, int count, Long leagueId, List<Long> teamIds);

    @Query(value = "select * from match_list where original_schedule_at <= ?1 and league_id = ?4 and (json_value(opponents,'$[0].opponent.id') in ?5 or json_value(opponents,'$[1].opponent.id') in ?5) order by original_schedule_at limit ?2, ?3", nativeQuery = true)
    List<MatchEntity> findAllBeforeMatchByTeamIds(LocalDateTime dateTime, int start, int count, Long leagueId, List<Long> teamIds);
}
