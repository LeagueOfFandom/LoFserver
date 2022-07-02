package com.lofserver.soma.repository;

import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.TeamUserEntity;
import com.lofserver.soma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamUserRepository extends JpaRepository<TeamUserEntity,Long> {

    @Query("select TeamEntity from TeamUserEntity where TeamUserEntity.userEntity = ?1")
    List<TeamEntity> findTeamEntityByUserEntity(UserEntity userEntity);
}
