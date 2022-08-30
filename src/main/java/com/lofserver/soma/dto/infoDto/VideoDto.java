package com.lofserver.soma.dto.infoDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideoDto {
    private String videoUrl;
    private String videoTitle;
    private String videoPublishedAt;
    private String videoChannel;
}
