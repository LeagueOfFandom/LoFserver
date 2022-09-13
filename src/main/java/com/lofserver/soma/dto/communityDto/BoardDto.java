package com.lofserver.soma.dto.communityDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardDto {
    //게시글 작성 시
    private String title;
    private String contents;
    private String creatorId;
    private String subject;
}
