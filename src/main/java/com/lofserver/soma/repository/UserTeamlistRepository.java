package com.lofserver.soma.repository;

import com.lofserver.soma.domain.UserTeamlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTeamlistRepository extends JpaRepository<UserTeamlistEntity, Long> {
    @Query(value = "SELECT uT from UserTeamlistEntity uT where uT.userId = ?1")
    List<UserTeamlistEntity> findAllByUserId(Long id);

}
