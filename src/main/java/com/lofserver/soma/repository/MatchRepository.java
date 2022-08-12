package com.lofserver.soma.repository;


import com.lofserver.soma.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
    @Query("select m.id from MatchEntity m")
    List<Long> findAllId();

    @Query(value = "select id from match_list where json_value(opponents, '$.opponent.id') = ?1", nativeQuery = true)
    List<Long> findAllByTeamId(Long teamId);
}
