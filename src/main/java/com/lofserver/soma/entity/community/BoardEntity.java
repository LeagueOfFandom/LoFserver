package com.lofserver.soma.entity.community;

import com.lofserver.soma.dto.communityDto.CommentDto;
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
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long boardId;

    @Column(name="title")
    private String title;

    @Column(name="contents")
    private String contents;

    @Column(name="view_count")
    private int viewCount;

    @Column(name="creator_id")
    private String creatorId;

    @Column(name="created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name="updater_id")
    private String updaterId;

    @Column(name="updated_datetime")
    private LocalDateTime updatedDatetime;

    @Type(type = "json")
    @Column(name = "comments", columnDefinition = "json")
    private List<CommentDto> comments;
}
