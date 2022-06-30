package com.lofserver.soma.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserIdDto {
    private Long userId;

    public UserIdDto(Long userId) {
        this.userId = userId;
    }
}
