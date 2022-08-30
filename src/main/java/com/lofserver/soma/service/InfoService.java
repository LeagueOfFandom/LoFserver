package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.teamRank.TeamRanking;
import com.lofserver.soma.controller.v1.response.teamRank.TeamRankingList;
import com.lofserver.soma.dto.TeamRankDto;
import com.lofserver.soma.entity.MatchEntity;
import com.lofserver.soma.entity.TeamEntity;
import com.lofserver.soma.entity.TeamRankingEntity;
import com.lofserver.soma.entity.info.VideoEntity;
import com.lofserver.soma.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InfoService {
    private final VideoRepository videoRepository;

    public ResponseEntity<?> getHighlightVideo(){
        log.info("getHighlightVideo 실행");

        List<VideoEntity> videoEntityList = videoRepository.findAll();
        if(videoEntityList == null){
            log.info("videoEntityList is null");
            return new ResponseEntity<>("영상 리스트 없음", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(videoEntityList, HttpStatus.OK);
    }

}
