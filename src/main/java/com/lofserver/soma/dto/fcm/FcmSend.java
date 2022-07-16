package com.lofserver.soma.dto.fcm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class FcmSend {
    private String to;
    private FcmData data;

    public FcmSend(String to, FcmData data) {
        this.to = to;
        this.data = data;
    }
}
