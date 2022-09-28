package com.lofserver.soma.repository;


import com.lofserver.soma.entity.MatchDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchDetailRepository extends JpaRepository<MatchDetailEntity, Long> {
}
