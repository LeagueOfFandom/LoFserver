package com.lofserver.soma.repository;

import com.lofserver.soma.entity.community.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
