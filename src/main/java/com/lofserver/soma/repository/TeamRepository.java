package com.lofserver.soma.repository;

import com.lofserver.soma.domain.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity,Long> {
}
