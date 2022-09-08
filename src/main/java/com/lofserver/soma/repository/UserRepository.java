package com.lofserver.soma.repository;

import com.lofserver.soma.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    @Query(value = "select * from lof_user where json_contains(team_list,?1) or json_value(selected,concat('$.',?2)) = true",nativeQuery = true)
    List<UserEntity> findAllByTeamIdAndMatchId(Long teamId, Long matchId);
}
