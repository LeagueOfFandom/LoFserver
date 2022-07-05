package com.lofserver.soma.repository;

import com.lofserver.soma.entity.MatchLckEntity;
import com.lofserver.soma.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchLckRepository extends JpaRepository<MatchLckEntity,Long> {
    List<MatchLckEntity> findAllByAwayIdOrHomeId(TeamEntity awayId, TeamEntity homeId);
}
