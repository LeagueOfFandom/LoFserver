package com.lofserver.soma.repository;

import com.lofserver.soma.domain.UserTeamListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTeamlistRepository extends JpaRepository<UserTeamListEntity, Long> {
    List<UserTeamListEntity> findAllByUserId(Long id);
}
