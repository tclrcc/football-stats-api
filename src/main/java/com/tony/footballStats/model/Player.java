package com.tony.footballStats.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "players")
public class Player {
    @Id
    private Long id; // On garde l'ID de l'API

    private String name;

    @Enumerated(EnumType.STRING)
    private Position position;

    private String nationality;
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
