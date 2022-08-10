package com.lofserver.soma.entity.match;

import lombok.Getter;

@Getter
public class MatchDetailSet {
    private String homeTop;
    private String homeMid;
    private String homeJungle;
    private String homeADC;
    private String homeSupport;
    private String awayTop;
    private String awayMid;
    private String awayJungle;
    private String awayADC;
    private String awaySupport;
    private String homeKill;
    private String awayKill;
    private String homeGold;
    private String awayGold;
    private String homeHerald;
    private String awayHerald;
    private String homeDragon;
    private String awayDragon;
    private String homeBaron;
    private String awayBaron;
    private String homeTower;
    private String awayTower;
    private String homeInhibitor;
    private String awayInhibitor;

    public MatchDetailSet(String homeTop, String homeMid, String homeJungle, String homeADC, String homeSupport, String awayTop, String awayMid, String awayJungle, String awayADC, String awaySupport, String homeKill, String awayKill, String homeGold, String awayGold, String homeHerald, String awayHerald, String homeDragon, String awayDragon, String homeBaron, String awayBaron, String homeTower, String awayTower, String homeInhibitor, String awayInhibitor) {
        this.homeTop = homeTop;
        this.homeMid = homeMid;
        this.homeJungle = homeJungle;
        this.homeADC = homeADC;
        this.homeSupport = homeSupport;
        this.awayTop = awayTop;
        this.awayMid = awayMid;
        this.awayJungle = awayJungle;
        this.awayADC = awayADC;
        this.awaySupport = awaySupport;
        this.homeKill = homeKill;
        this.awayKill = awayKill;
        this.homeGold = homeGold;
        this.awayGold = awayGold;
        this.homeHerald = homeHerald;
        this.awayHerald = awayHerald;
        this.homeDragon = homeDragon;
        this.awayDragon = awayDragon;
        this.homeBaron = homeBaron;
        this.awayBaron = awayBaron;
        this.homeTower = homeTower;
        this.awayTower = awayTower;
        this.homeInhibitor = homeInhibitor;
        this.awayInhibitor = awayInhibitor;
    }
}
