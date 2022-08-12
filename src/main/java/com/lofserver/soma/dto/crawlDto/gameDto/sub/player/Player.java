package com.lofserver.soma.dto.crawlDto.gameDto.sub.player;

import com.lofserver.soma.dto.crawlDto.matchDto.sub.Opponent;
import lombok.Getter;

import java.util.List;

@Getter
public class Player {
    private PhysicalDamage physical_damage;
    private Ward wards;
    private KillSeries kills_series;
    private List<Item> items;
    private PlayerDetails player;
    private Champion champion;
    private Long gold_spent;
    private Long largest_critical_strike;
    private Long game_id;
    private Long minions_killed;
    private List<Spell> spells;
    private Long level;
    private Damage total_damage;
    private Opponent opponent;
    private Damage true_damage;
    private Damage magic_damage;
    private Long total_heal;
    private KillCounter kills_counter;
    private Long kills;
    private Long gold_earned;
    private Opponent team;
    private Long deaths;
    private Long largest_multi_kill;
    private Long player_id;
    private Long assists;
    private Long wards_placed;
    private String role;
    private Long creep_score;

}
