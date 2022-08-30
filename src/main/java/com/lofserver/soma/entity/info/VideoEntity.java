package com.lofserver.soma.entity.info;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Table(name="video")
public class VideoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long videoId;

    @Column(name="video_url")
    private String videoUrl;

    @Column(name="video_title")
    private String videoTitle;

    @Column(name="video_description")
    private String videoDescription;

    @Column(name="video_thumbnail")
    private String videoThumbnail;

    @Column(name="video_duration")
    private String videoDuration;

    @Column(name="video_view_count")
    private String videoViewCount;

    @Column(name="video_like_count")
    private String videoLikeCount;

    @Column(name="video_dislike_count")
    private String videoDislikeCount;

    @Column(name="video_published_at")
    private String videoPublishedAt;

    @Column(name="video_channel")
    private String videoChannel;
  
    //- 링크
    //- 종류 ex. 경기 하이라이트/ 선수 개인/ 팀 영상
    //- 업로드 된 날짜
    //- 출처 ex. 유튜브, 트위치
    //- 기타 정보 ex. 어떤 팀이 관련되어 있는지
    //- 우리 앱 내에서 조회수(프론트에서 클릭하면 가져올 수 있는지 물어보기)
    //- 우리 앱 내에서 view수
}
