package com.lofserver.soma.service.view;

import com.lofserver.soma.controller.v1.response.CommonItem;
import com.lofserver.soma.controller.v1.response.community.CommunityViewObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommunityViewService {

    public CommonItem getCommunityListTest(){
        CommunityViewObject communityViewObject = CommunityViewObject.builder()
                .nickname("testNickname")
                .profileImg("https://lh3.googleusercontent.com/a/ALm5wu0owKbQ9im6-ViZ9WKUHt2RwqGVLlx1i59ex1CZ=s96-c")
                .time(LocalDateTime.now())
                .content("test 블라블라")
                .build();
        return new CommonItem(communityViewObject);
    }
}
