package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "leagues")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // ex: Premier League

    @Column(unique = true)
    private String code;    // ex: PL, FL1, SA

    private String country; // ex: Angleterre
    private String logoUrl;

    private boolean active = true; // Pour activer/désactiver la synchro

    private LocalDateTime lastUpdated;

    // Relation 1 Ligue → N Équipes
    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Team> teams;
}
