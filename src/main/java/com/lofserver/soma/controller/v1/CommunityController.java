package com.lofserver.soma.controller.v1;

import com.lofserver.soma.dto.communityDto.BoardDto;
import com.lofserver.soma.entity.community.BoardEntity;
import com.lofserver.soma.service.CommunityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses({
        @ApiResponse(code = 200, message = "success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommunityController{
    private final CommunityService communityService;

    //페이징 추가해서 나눠서 주기
    @ApiOperation(value = "게시판 목록 조회 Api", notes = "client에서 GET하면 모든 게시글을 반환한다.")
    @GetMapping("/board")
    public ResponseEntity<?> getBoardList() throws Exception {
        return communityService.getBoardList();
    }

    @ApiOperation(value = "게시판 글 작성 Api", notes = "client에서 게시글을 작성한 후 POST하면 server에 저장한다.")
    @PostMapping(value="/board/write")
    public ResponseEntity<String> insertBoard(@RequestBody BoardDto boardDto) throws Exception {
        return communityService.writeBoard(boardDto);
    }

    @ApiOperation(value = "게시글 상세화면 조회 Api", notes = "client에서 게시글 id를 GET하면 해당 게시글을 반환한다.")
    @GetMapping(value="/board")
    public ResponseEntity<?> getBoardDetail(@RequestParam(value="boardId")Long boardId) throws Exception {
        return communityService.getBoardDetail(boardId);
    }

    //PUT, DELETE
}
