package com.lofserver.soma.entity.community;

import com.lofserver.soma.dto.communityDto.BoardDto;
import com.lofserver.soma.dto.communityDto.CommentDto;
import com.lofserver.soma.entity.BaseTimeEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name="board")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class BoardEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long boardId;

    @Column(name="title")
    private String title;

    @Column(name="contents")
    private String contents;

    @Column(name="subject") //게시판 종류
    private String subject;

    @Column(name="view_count")
    private Long viewCount;

    @Column(name="creator_id")
    private String creatorId;

    @Column(name="deleted_datetime")
    private LocalDateTime deletedDatetime;

    @Type(type = "json")
    @Column(name = "comments", columnDefinition = "json")
    private List<CommentDto> comments;

    public BoardEntity(BoardDto boardDto, LocalDateTime createdDatetime) {
        this.title = boardDto.getTitle();
        this.contents = boardDto.getContents();
        this.creatorId = boardDto.getCreatorId();
        this.subject= boardDto.getSubject();
    }
}
