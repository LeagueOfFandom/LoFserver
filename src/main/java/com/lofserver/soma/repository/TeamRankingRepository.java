package com.lofserver.soma.repository;

import com.lofserver.soma.entity.TeamRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRankingRepository extends JpaRepository<TeamRankingEntity, Long> {

    @Query(value= "SELECT * FROM team_ranking WHERE year = :year and  season = :season and league =:league ", nativeQuery = true)
    List<TeamRankingEntity> findByYearSeasonLeague(@Param("year")String year, @Param("season")String season, @Param("league")String league);

}
