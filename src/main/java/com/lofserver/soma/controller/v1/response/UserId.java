package com.lofserver.soma.controller.v1.response;


import lombok.Getter;

@Getter
public class UserId {
    private String JwtToken;
    private boolean isNewUser;

    public UserId(String jwtToken, boolean isNewUser) {
        JwtToken = jwtToken;
        this.isNewUser = isNewUser;
    }
}
