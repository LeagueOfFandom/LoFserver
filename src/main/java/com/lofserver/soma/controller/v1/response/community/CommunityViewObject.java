package com.lofserver.soma.controller.v1.response.community;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommunityViewObject {
    private String nickname;
    private String profileImg;
    private LocalDateTime time;
    private String content;


    @Builder
    public CommunityViewObject(String nickname, String profileImg, LocalDateTime time, String content) {
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.time = time;
        this.content = content;
    }
}
