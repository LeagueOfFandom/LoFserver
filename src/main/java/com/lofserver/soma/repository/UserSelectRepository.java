package com.lofserver.soma.repository;

import com.lofserver.soma.entity.UserEntity;
import com.lofserver.soma.entity.UserSelectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSelectRepository extends JpaRepository<UserSelectEntity, Long> {
    UserSelectEntity findByMatchId(Long matchId);

    List<UserSelectEntity> findAllByUserEntity(UserEntity userEntity);
}
