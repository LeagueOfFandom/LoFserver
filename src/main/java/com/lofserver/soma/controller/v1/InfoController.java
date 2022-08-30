package com.lofserver.soma.controller.v1;

import com.lofserver.soma.controller.v1.response.matchDetail.TeamVsTeam;
import com.lofserver.soma.service.InfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses({
        @ApiResponse(code = 200, message = "success"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController
@RequiredArgsConstructor
@Slf4j
public class InfoController {
    private final InfoService infoService;

    @ApiOperation(value = "하이라이트 영상 Api", notes = "client에서 호출하면 server에서 하이라이트 영상 링크를 전달한다.")
    @GetMapping("/highlightVideo")
    public ResponseEntity<?> getHightlightVideo(){
        return infoService.getHighlightVideo();
    }
}
