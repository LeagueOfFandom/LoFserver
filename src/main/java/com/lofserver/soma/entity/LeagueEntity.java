package com.lofserver.soma.entity;

import com.lofserver.soma.dto.leagueDto.Series;
import com.lofserver.soma.dto.leagueDto.VideoGame;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "league")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class LeagueEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "modified_at")
    private LocalDateTime modified_at;

    @Column(name = "name")
    private String name;

    @Type(type = "json")
    @Column(name = "series", columnDefinition = "json")
    private List<Series> series;

    @Column(name = "latest_series_id")
    private Long latest_series_id;
    @Column(name = "slug")
    private String slug;

    @Column(name = "url")
    private String url;

    @Type(type = "json")
    @Column(name = "videogame", columnDefinition = "json")
    private VideoGame videogame;

}