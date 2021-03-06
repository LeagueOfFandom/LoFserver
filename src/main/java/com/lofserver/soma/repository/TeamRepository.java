package com.lofserver.soma.repository;

import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    TeamEntity findByTeamName(String teamName);
    @Query("select te.teamId from TeamEntity te")
    List<Long> findAllId();
}
