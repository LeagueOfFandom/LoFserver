package com.lofserver.soma.entity.community;

import com.lofserver.soma.dto.communityDto.ViewerDto;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Table(name="board_view_count")
@TypeDef(name="json", typeClass = JsonStringType.class)
@NoArgsConstructor
public class ViewCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long viewCountId;

    @Id
    @Column(name="board_id")
    private Long boardId;

    @Type(type = "json")
    @Column(name = "viewers", columnDefinition = "json")
    private List<ViewerDto> viewers;

}
