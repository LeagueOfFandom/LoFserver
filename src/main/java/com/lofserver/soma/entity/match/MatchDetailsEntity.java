package com.lofserver.soma.entity.match;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Table(name = "match_lck_detail")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class MatchDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "match_id")
    private Long matchId;

    @Type(type = "json")
    @Column(name = "match_detail_list", columnDefinition = "json")
    private List<MatchDetailSet> matchDetailSetList = new ArrayList<>();

    public MatchDetailsEntity(Long matchId, List<MatchDetailSet> matchDetailSetList) {
        this.matchId = matchId;
        this.matchDetailSetList = matchDetailSetList;
    }
}
