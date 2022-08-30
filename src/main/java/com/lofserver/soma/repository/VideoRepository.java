package com.lofserver.soma.repository;

import com.lofserver.soma.entity.info.VideoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {
}
