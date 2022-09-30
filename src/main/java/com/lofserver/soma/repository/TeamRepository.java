package com.lofserver.soma.repository;


import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    @Query("select t.id from TeamEntity t where t.seriesId = ?1")
    List<Long> findAllIdBySeriesId(Long id);

    @Query("select t.seriesId from TeamEntity t where t.id in ?1")
    List<Long> findSerieIdListByTeamIdList(List<Long> teamIdList);
}
