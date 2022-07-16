package com.lofserver.soma.controller.v1;

import com.lofserver.soma.service.CrawlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CrawlController {
    private final CrawlService crawlService;

    @GetMapping("/match/entity")
    public void getMatchScheduleList(HttpServletRequest request){
        crawlService.getMatchScheduleList();
        return;
    }
}
