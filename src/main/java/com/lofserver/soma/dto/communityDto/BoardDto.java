package com.lofserver.soma.dto.communityDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    //게시글 작성 시
    private String title;
    private String contents;
    private String creatorId;
    private String subject;
}
