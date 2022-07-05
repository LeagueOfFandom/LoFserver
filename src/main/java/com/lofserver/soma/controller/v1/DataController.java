package com.lofserver.soma.controller.v1;

import com.lofserver.soma.dto.MatchDto;
import com.lofserver.soma.service.DataService;
import com.lofserver.soma.service.LofService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiResponses({
        @ApiResponse(code = 200, message = "!!Success!!"),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
@RestController("/api/v1/data")
@Slf4j
public class DataController {
    @Autowired
    private DataService dataService;

    @ApiOperation(value = "Match 등록 Api", notes = "리그 경기 내역 등록 API")
    @PostMapping("/setMatch")
    public void getUserId(@RequestBody List<MatchDto> matchDto){
        log.info(matchDto.get(0).toString());
        dataService.setMatch(matchDto);
    }

}
