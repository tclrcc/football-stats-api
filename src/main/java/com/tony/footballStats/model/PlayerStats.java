package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "player_stats")
public class PlayerStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;      // ex: 2023
    private Integer appearances; // Matchs joués
    private Integer minutes;     // Minutes jouées
    private Integer goals;
    private Integer assists;
    private Integer yellowCards;
    private Integer redCards;

    // Note moyenne (ex: 7.4)
    private Double rating;

    // Lien vers notre joueur existant
    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;
}
