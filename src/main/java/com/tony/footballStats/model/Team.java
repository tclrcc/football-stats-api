package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "teams")
// On exclut 'league', 'coach' et 'players' pour éviter les boucles infinies lors des logs/debug
@EqualsAndHashCode(exclude = {"coach", "players", "league"})
@ToString(exclude = {"coach", "players", "league"})
public class Team {
    @Id
    private Long id;

    private String name;
    private String shortName;
    private String tla;
    private String crestUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String website;
    private Integer foundedYear;
    private String clubColors;
    private String stadium;

    private String areaName;
    private String areaFlagUrl;

    private LocalDateTime lastUpdated;

    // --- RELATIONS ---

    // AJOUT ICI : Le lien vers la Ligue
    @ManyToOne
    @JoinColumn(name = "league_id")
    private League league;

    @OneToOne(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Coach coach;

    // 1 Equipe a N Joueurs.
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Player> players = new ArrayList<>();
}
