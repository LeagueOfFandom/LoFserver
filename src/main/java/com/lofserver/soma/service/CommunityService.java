package com.lofserver.soma.service;

import com.lofserver.soma.dto.communityDto.BoardDto;
import com.lofserver.soma.entity.community.BoardEntity;
import com.lofserver.soma.entity.info.VideoEntity;
import com.lofserver.soma.repository.BoardRepository;
import com.lofserver.soma.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityService {

    private final BoardRepository boardRepository;

    public ResponseEntity<String> writeBoard(BoardDto boardDto) {
        //boardDto 내용의 유효성 검사(하나라도 null값이 있으면 안된다)
        if (boardDto.getTitle().isEmpty() || !boardDto.getContents().isEmpty() || boardDto.getCreatorId().isEmpty()) {
            log.info("writeBoard: " + "post 내용이 비어있음");
            return new ResponseEntity<>("해당 match id 없음", HttpStatus.BAD_REQUEST);
        }

        //boardDto 내용 boardEntity 저장
        boardRepository.save(new BoardEntity(boardDto, LocalDateTime.now()));
        return new ResponseEntity<>("게시글 작성 성공", HttpStatus.OK);
    }

    public ResponseEntity<?> getBoardList() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        if(boardEntityList == null){
            log.info("boardEntityList is null");
            return new ResponseEntity<>("게시판 리스트 없음", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(boardEntityList, HttpStatus.OK);
    }

}
