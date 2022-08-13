package com.lofserver.soma.repository;


import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("select m.id from MatchEntity m")
    List<Long> findAllId();

    @Query(value = "select id from match_list where json_value(opponents, '$.opponent.id') = ?1", nativeQuery = true)
    List<Long> findAllByTeamId(Long teamId);

    // team 최근 성적
    @Query(value = "select * from match_list where status='finished' and json_value(results, '$[0].team_id') = ?1 or status='finished' and json_value(results, '$[1].team_id') = ?1 ORDER BY id DESC limit 5",nativeQuery = true)
    List<MatchEntity> findRecentGamesByTeamId(Long TeamId);
}
