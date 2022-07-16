package com.lofserver.soma.entity;


import com.lofserver.soma.controller.v1.response.team.UserTeamInfo;
import lombok.Getter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Table(name = "team")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long teamId; //임의로 생성된 team id.
    @Column(name = "name")
    private String teamName; //team 이름.
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
