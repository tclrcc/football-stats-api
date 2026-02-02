package com.tony.footballStats.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "teams")
public class Team {
    @Id
    private Long id;

    private String name;

    private String tla;         // ex: MCI
    private String crestUrl;    // L'URL du logo
    private String address;     // L'adresse du club
    private String stadium;
    private Integer foundedYear;

    private LocalDateTime lastUpdated;

    private Integer leagueTitles;
    private Integer championsLeagueTitles;

    @ManyToOne
    @JoinColumn(name = "league_id")
    @JsonIgnore
    private League league;
}
