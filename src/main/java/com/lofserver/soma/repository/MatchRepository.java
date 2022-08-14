package com.lofserver.soma.repository;


import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("select m.id from MatchEntity m")
    List<Long> findAllId();

    @Query(value = "select * from match_list where status='finished' and ( (json_value(opponents,'$[0].opponent.id') = ?1 and json_value(opponents,'$[1].opponent.id') = ?2) or (json_value(opponents,'$[0].opponent.id') = ?2 and json_value(opponents,'$[1].opponent.id') = ?1))", nativeQuery = true)
    List<MatchEntity> findAllByTeamIds(Long homeId, Long awayId);


    @Query(value = "select * from match_list where status='finished' and json_value(results, '$[0].team_id') = ?1 or status='finished' and json_value(results, '$[1].team_id') = ?1 ORDER BY id DESC limit 5",nativeQuery = true)
    List<MatchEntity> findRecentGamesByTeamId(Long TeamId);

    @Query(value = "select * from match_list where original_schedule_at >= ?1 and league_id = ?4 and (json_value(opponents,'$[0].opponent.id') in ?5 or json_value(opponents,'$[1].opponent.id') in ?5) order by original_schedule_at desc limit ?2, ?3", nativeQuery = true)
    List<MatchEntity> findAllAfterMatchByTeamIds(LocalDateTime dateTime, int start, int count, Long leagueId, List<Long> teamIds);

    @Query(value = "select * from match_list where original_schedule_at <= ?1 and league_id = ?4 and (json_value(opponents,'$[0].opponent.id') in ?5 or json_value(opponents,'$[1].opponent.id') in ?5) order by original_schedule_at limit ?2, ?3", nativeQuery = true)
    List<MatchEntity> findAllBeforeMatchByTeamIds(LocalDateTime dateTime, int start, int count, Long leagueId, List<Long> teamIds);
}
