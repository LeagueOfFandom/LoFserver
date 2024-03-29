package com.lofserver.soma.service;

import com.lofserver.soma.dto.communityDto.BoardDto;
import com.lofserver.soma.entity.community.BoardEntity;
import com.lofserver.soma.repository.BoardRepository;
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

    //게시글 작성
    public ResponseEntity<String> writeBoard(BoardDto boardDto) {
        //boardDto 내용의 유효성 검사(하나라도 null값이 있으면 안된다)
        if (boardDto.getTitle() == null || boardDto.getContents() == null || boardDto.getCreatorId() == null || boardDto.getSubject() == null) {
            log.info("writeBoard: " + "post 내용이 비어있음");
            return new ResponseEntity<>("입력내용이 없음", HttpStatus.BAD_REQUEST);
        }
        //getCreateorId와 getSubject의 유효성을 검사해야 함
        //getSubject는 enum으로 만들어서 관리하는게 편리할듯

        //boardDto 내용 boardEntity 저장
        log.info(boardDto.getSubject().toString());
        boardRepository.save(new BoardEntity(boardDto, LocalDateTime.now()));
        return new ResponseEntity<>("게시글 작성 성공", HttpStatus.OK);
    }

    //게시글 리스트 보기
    public ResponseEntity<?> getBoardList() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();

        if(boardEntityList == null){
            log.info("boardEntityList is null");
            return new ResponseEntity<>("게시판 리스트 없음", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(boardEntityList, HttpStatus.OK);
    }

    //게시글 하나 상세정보 보기
    public ResponseEntity<?> getBoardDetail(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).orElse(null);

        if(boardEntity == null){
            log.info("boardEntity is null");
            return new ResponseEntity<>("해당 게시글 없음", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(boardEntity, HttpStatus.OK);
    }

}
