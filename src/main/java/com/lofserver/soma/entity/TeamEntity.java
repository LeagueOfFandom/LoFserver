package com.lofserver.soma.entity;


import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long teamId; //임의로 생성된 team id.

    @Column(name = "name")
    private String teamName;

    @Type(type = "json")
    @Column(name = "name_list", columnDefinition = "json")
    private Map<String, String> teamNameList = new HashMap<>(); //team 이름(여러 언어).

    @Column(name = "img")
    private String teamImg; //team의 img link.
    @Type(type = "json")
    @Column(name = "match_list", columnDefinition = "json")
    private Set<Long> teamMatchList = new HashSet<>(); //team의 경기 lisk.
    public void delMatch(Long matchId){
        teamMatchList.remove(matchId);
    }
    public void addMatch(Long matchId){
        teamMatchList.add(matchId);
    }

}
