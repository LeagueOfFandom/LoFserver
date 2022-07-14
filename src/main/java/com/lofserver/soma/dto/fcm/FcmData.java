package com.lofserver.soma.dto.fcm;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FcmData {
    private String title;
    private String message;

    public FcmData(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
