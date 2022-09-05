package com.lofserver.soma.dto.communityDto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CommentDto {
    private Long commentId;
    private String title;
    private String contents;
    private Long viewCount;
    private String creatorId;
    private LocalDateTime createdDatetime;
    private String updaterId;
    private LocalDateTime updatedDatetime;
}
