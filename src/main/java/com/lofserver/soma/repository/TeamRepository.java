package com.lofserver.soma.repository;


import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    @Query("select t.id from TeamEntity t")
    List<Long> findAllId();

    @Query("select t from TeamEntity t where t.series_id = ?1")
    List<TeamEntity> findAllBySeries_Id(Long series_id);

    @Query(value = "select * from team_list where name = ?1",nativeQuery = true)
    TeamEntity findByTeamName(String teamName);
}
