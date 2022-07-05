package com.lofserver.soma.repository;

import com.lofserver.soma.entity.MatchLckEntity;
import com.lofserver.soma.entity.MatchUserEntity;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MatchUserRepository extends JpaRepository<MatchUserEntity, Long> {
    MatchUserEntity findByMatchLckEntityAndUserEntity(MatchLckEntity matchLckEntity, UserEntity userEntity);
    @Transactional
    void deleteMatchUserEntityByMatchLckEntityAndUserEntity(MatchLckEntity matchLckEntity, UserEntity userEntity);

    @Transactional
    void deleteMatchUserEntitiesByMatchLckEntity_HomeIdOrMatchLckEntity_AwayId(TeamEntity homeId, TeamEntity awayId);
}
