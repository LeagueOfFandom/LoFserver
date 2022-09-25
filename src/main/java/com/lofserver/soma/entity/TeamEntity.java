package com.lofserver.soma.entity;

import com.lofserver.soma.dto.pandaScoreDto.gameDto.sub.player.PlayerDetails;
import com.lofserver.soma.dto.pandaScoreDto.teamsDetailDto.sub.Status;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "team_list")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk")
    private Long pk;
    @Column(name = "id")
    private Long id;

    @Column(name = "acronym")
    private String acronym;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Type(type = "json")
    @Column(name = "players", columnDefinition = "json")
    private List<PlayerDetails> players;

    @Type(type = "json")
    @Column(name = "status", columnDefinition = "json")
    private Status status;

    @Column(name = "series_id")
    private Long seriesId;

}

