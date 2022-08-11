package com.lofserver.soma.repository;


import com.lofserver.soma.entity.match.MatchDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchDetailsRepository extends JpaRepository<MatchDetailsEntity, Long> {
    MatchDetailsEntity findByMatchId(Long matchId);
}
