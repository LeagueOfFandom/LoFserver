package com.lofserver.soma.repository;

import com.lofserver.soma.entity.TeamEntity;
import lombok.ToString;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}
