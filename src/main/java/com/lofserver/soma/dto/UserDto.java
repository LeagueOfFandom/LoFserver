package com.lofserver.soma.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private String googleAccessToken;
    private String fcmToken;
}
