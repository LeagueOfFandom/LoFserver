package com.lofserver.soma.repository;

import com.lofserver.soma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findUserEntityByDeviceId(String deviceId);
}
