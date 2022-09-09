package com.lofserver.soma.repository;

import com.lofserver.soma.entity.LeagueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<LeagueEntity, Long> {
    LeagueEntity findByName(String name);
}
