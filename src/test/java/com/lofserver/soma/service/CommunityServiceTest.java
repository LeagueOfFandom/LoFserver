package com.lofserver.soma.service;

import com.lofserver.soma.dto.communityDto.BoardDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CommunityServiceTest {

    @Autowired CommunityService communityService;

    @Test
    public void 게시글작성() throws Exception {
        //given
        BoardDto boardDto = new BoardDto("제목", "내용", "작성자", "T1");

        BoardDto boardDto1 = new BoardDto(); //제목이 없는 경우-null
        boardDto1.setContents("내용1");
        boardDto1.setCreatorId("작성자1");
        boardDto1.setSubject("all");

        BoardDto boardDto2 = new BoardDto(); //내용이 없는 경우-null
        boardDto2.setTitle("제목2");
        boardDto2.setCreatorId("작성자2");
        boardDto2.setSubject("all");


        //when
        ResponseEntity<String> responseEntity = communityService.writeBoard(boardDto);
        ResponseEntity<String> responseEntity1 = communityService.writeBoard(boardDto1);
        ResponseEntity<String> responseEntity2 = communityService.writeBoard(boardDto2);

        //then
        assertEquals("게시글 작성 성공", responseEntity.getBody());
        assertEquals("입력내용이 없음", responseEntity1.getBody());
        assertEquals("입력내용이 없음", responseEntity2.getBody());
    }


}