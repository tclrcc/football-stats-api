package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "leagues")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // ex: Premier League
    private String country; // ex: Angleterre
    private String logoUrl;

    // Relation 1 Ligue → N Équipes
    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Team> teams;
}
