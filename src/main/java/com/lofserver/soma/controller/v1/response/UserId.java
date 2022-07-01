package com.lofserver.soma.controller.v1.response;


import lombok.Getter;

@Getter
public class UserId {
    private Long userId;
    private boolean check;

    public UserId(Long userId, boolean check) {
        this.userId = userId;
        this.check = check;
    }
}
