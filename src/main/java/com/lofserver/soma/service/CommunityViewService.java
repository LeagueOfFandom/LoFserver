package com.lofserver.soma.service;

import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.community.CommunityViewObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommunityViewService {

    public CommonItem getCommunityListTest(){
        CommunityViewObject communityViewObject = CommunityViewObject.builder()
                .nickname("testNickname")
                .profileImg("")
                .time(LocalDateTime.now())
                .content("test 블라블라")
                .build();
        return new CommonItem(communityViewObject);
    }
}
