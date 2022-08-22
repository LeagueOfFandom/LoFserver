package com.lofserver.soma.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "champion")
public class ChampionEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    public ChampionEntity(Long id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }
}
