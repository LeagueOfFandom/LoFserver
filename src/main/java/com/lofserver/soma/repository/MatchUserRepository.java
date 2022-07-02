package com.lofserver.soma.repository;

import com.lofserver.soma.entity.MatchLckEntity;
import com.lofserver.soma.entity.MatchUserEntity;
import com.lofserver.soma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchUserRepository extends JpaRepository<MatchUserEntity, Long> {
    MatchUserEntity findByMatchLckEntityAndUserEntity(MatchLckEntity matchLckEntity, UserEntity userEntity);
    MatchUserEntity deleteMatchUserEntityByMatchLckEntityAndUserEntity(MatchLckEntity matchLckEntity, UserEntity userEntity);

    @Query("delete from MatchUserEntity mu where mu.matchLckEntity.homeId = ?1 or mu.matchLckEntity.awayId = ?1")
    MatchUserEntity deleteMatchUserEntitiesByTeamId(Long teamId);
}
