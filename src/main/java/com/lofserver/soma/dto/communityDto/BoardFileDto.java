package com.lofserver.soma.dto.communityDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardFileDto {
    private int idx;
    private int boardIdx;
    private String originalFileName;
    private String storedFilePath;
    private long fileSize;
}