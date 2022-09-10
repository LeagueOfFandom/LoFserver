package com.lofserver.soma.repository;

import com.lofserver.soma.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
    LeagueEntity findByName(String name);

    @Query("select l from LeagueEntity l where l.latest_series_id = ?1")
    LeagueEntity findBySeriesId(Long id);
}
