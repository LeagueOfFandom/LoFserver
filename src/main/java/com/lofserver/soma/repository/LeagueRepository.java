package com.lofserver.soma.repository;

import com.lofserver.soma.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
    LeagueEntity findByName(String name);
    @Query("select l.id from LeagueEntity l where l.latestSeriesId in ?1")
    List<Long> findLeagueIdListBySerieIdList(List<Long> serieIdList);

    @Query("select l from LeagueEntity l where l.latestSeriesId = ?1")
    LeagueEntity findBySeriesId(Long id);
}
